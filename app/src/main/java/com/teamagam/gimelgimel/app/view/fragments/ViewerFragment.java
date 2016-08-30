package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageMapEntitiesViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.ViewerFragmentViewModel;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGImageSender;
import com.teamagam.gimelgimel.app.network.services.IImageSender;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.utils.ImageUtil;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendMessageDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.GGMapGestureListener;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Viewer Fragmant that handles all map events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements
        SendGeographicMessageDialog.SendGeographicMessageDialogInterface,
        OnGGMapReadyListener, SendImageMessagePresenter.View {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final String IMAGE_URI_KEY = "IMAGE_CAMERA_URI";

    @BindView(R.id.gg_map_view)
    GGMapView mGGMapView;

    private VectorLayer mSentLocationsLayer;
    private VectorLayer mUsersLocationsLayer;
    private VectorLayer mReceivedLocationsLayer;

    private UsersLocationViewModel mUserLocationsVM;
    private ViewerFragmentViewModel mViewerFragmentViewModel;

    private MessageBroadcastReceiver mUserLocationReceiver;
    private BroadcastReceiver mLocationReceiver;
    private IImageSender mImageSender;

    private Uri mImageUri;
    private boolean mIsRestored;
    private Handler mHandler;
    private Runnable mPeriodicalUserLocationsRefreshRunnable;
    private MessageMapEntitiesViewModel mMessageLocationVM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessageLocationVM = mApp.getMessageMapEntitiesViewModel();
        mUserLocationsVM = mApp.getUserLocationViewModel();
        mViewerFragmentViewModel = new ViewerFragmentViewModel(this);
        mHandler = new Handler();
        mPeriodicalUserLocationsRefreshRunnable = new Runnable() {
            @Override
            public void run() {
                sLogger.v("Synchronizing user-locations symbolization");
                mUserLocationsVM.synchronizeToVectorLayer(mUsersLocationsLayer);
                mHandler.postDelayed(mPeriodicalUserLocationsRefreshRunnable,
                        Constants.USERS_LOCATION_REFRESH_FREQUENCY_MS);
            }
        };
    }


    @Override
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mSentLocationsLayer = new VectorLayer("vl2");
        mReceivedLocationsLayer = new VectorLayer("vlReceivedLocation");
        mUsersLocationsLayer = new VectorLayer("vlUsersLocation");

        mImageSender = new GGImageSender();

        mGGMapView.setGGMapGestureListener(new GGMapGestureListener(this, mGGMapView));

        mUserLocationReceiver = new MessageBroadcastReceiver(
                new UserLocationMessageHandler(), Message.USER_LOCATION);

        mLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras().containsKey(LocationFetcher.KEY_NEW_LOCATION_SAMPLE)) {
                    LocationSample locationSample = intent.getParcelableExtra(
                            LocationFetcher.KEY_NEW_LOCATION_SAMPLE);
                    putMyLocationPin(locationSample);
                }
            }
        };

        if (savedInstanceState != null) {
            mGGMapView.restoreViewState(savedInstanceState);
            mIsRestored = true;
        } else {
            mIsRestored = false;
        }

        secureGGMapViewInitialization();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        startPeriodicalUserLocationsRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopPeriodicalUserLocationRefresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mGGMapView.saveViewState(outState);
        outState.putParcelable(IMAGE_URI_KEY, mImageUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mImageUri = savedInstanceState.getParcelable(IMAGE_URI_KEY);
        }
    }

    @OnClick(R.id.message_fab)
    public void openSendMessageDialog() {
        sLogger.userInteraction("Send message button clicked");
        new SendMessageDialogFragment().show(getFragmentManager(), "sendMessageDialog");
    }

    @OnClick(R.id.camera_fab)
    public void startCameraActivity() {
        sLogger.userInteraction("Start camera activity button clicked");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // place where to store camera taken picture
        try {
            mImageUri = ImageUtil.getTempImageUri(mApp);
        } catch (IOException e) {
            sLogger.w("Can't create file to take picture!");
            return;
        }

        if (mImageUri != null) {
            sLogger.d(mImageUri.getPath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            //start camera intent
            if (takePictureIntent.resolveActivity(mApp.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            sLogger.w("image uri is null");
            Toast.makeText(mApp, "problem with taking images", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.locate_me_fab)
    public void zoomToLastKnownLocation() {
        sLogger.userInteraction("Locate me button clicked");

        LocationSample lastKnownLocation = LocationFetcher.getInstance(
                getActivity()).getLastKnownLocation();

        if (lastKnownLocation == null) {
            Toast.makeText(getActivity(), R.string.locate_me_fab_no_known_location,
                    Toast.LENGTH_SHORT).show();
        } else {
            PointGeometry location = lastKnownLocation.getLocation();

            location.altitude = Constants.LOCATE_ME_BUTTON_ALTITUDE_METERS;
            mGGMapView.zoomTo(location);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (isImageCaptured(resultCode)) {
                sLogger.userInteraction("Sending camera activity result");
                sendCapturedImageToServer();
            } else {
                sLogger.userInteraction("Camera activity returned with no captured image");
            }
        }
    }

    private void secureGGMapViewInitialization() {
        if (mGGMapView.isReady()) {
            onGGMapViewReady();
        } else {
            mGGMapView.setOnReadyListener(this);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cesium;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUserLocationReceiver != null) {
            MessageBroadcastReceiver.unregisterReceiver(getActivity(), mUserLocationReceiver);
        }
        if (mLocationReceiver != null) {
            LocationFetcher.getInstance(getActivity()).unregisterReceiver(mLocationReceiver);
        }
    }

    @Override
    public void drawSentPin(Message message) {
        Entity entity = mMessageLocationVM.addSentMessage(message);
        mSentLocationsLayer.addEntity(entity);
    }

    public void goToLocation(PointGeometry pointGeometry) {
        mGGMapView.flyTo(pointGeometry);
    }

    public GGMap getGGMap() {
        return mGGMapView;
    }

    @Override
    public void onGGMapViewReady() {
        if (!mIsRestored) {
            setInitialMapExtent();
        }

        mGGMapView.addLayer(mSentLocationsLayer);
        mGGMapView.addLayer(mUsersLocationsLayer);
        mGGMapView.addLayer(mReceivedLocationsLayer);

        mUserLocationsVM.synchronizeToVectorLayer(mUsersLocationsLayer);

        registerForLocationUpdates();
    }

    public void clearSentLocationsLayer() {
        mSentLocationsLayer.removeAllEntities();
    }

    public void clearReceivedLocationsLayer() {
        mReceivedLocationsLayer.removeAllEntities();
    }

    private void startPeriodicalUserLocationsRefresh() {
        mHandler.post(mPeriodicalUserLocationsRefreshRunnable);
    }

    private void stopPeriodicalUserLocationRefresh() {
        mHandler.removeCallbacks(mPeriodicalUserLocationsRefreshRunnable);
    }

    private boolean isImageCaptured(int resultCode) {
        return resultCode == Activity.RESULT_OK && mImageUri != null;
    }

    private void sendCapturedImageToServer() {
        LocationSample imageLocation = LocationFetcher.getInstance(
                getActivity()).getLastKnownLocation();
        String senderId = mApp.getPrefs().getString(R.string.user_name_text_key);
        if (imageLocation != null) {
            PointGeometry loc = imageLocation.getLocation();
            mViewerFragmentViewModel.sendImage(senderId, mImageUri.getPath(), loc.latitude, loc.longitude);
        } else {
            //mImageSender.sendImage(getActivity(), mImageUri, imageTime, loc);
            mViewerFragmentViewModel.sendImage(senderId, mImageUri.getPath());
        }
    }

    /**
     * Sets GGMapView extent to configured bounding box values
     */
    private void setInitialMapExtent() {
        float east = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_EAST;
        float west = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_WEST;
        float north = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_NORTH;
        float south = Constants.MAP_VIEW_INITIAL_BOUNDING_BOX_SOUTH;
        mGGMapView.setExtent(west, south, east, north);
    }

    private void registerForLocationUpdates() {
        //Register for new incoming users location messages
        MessageBroadcastReceiver.registerReceiver(getActivity(), mUserLocationReceiver);

        //Register for local location messages
        LocationFetcher.getInstance(getActivity()).registerReceiver(mLocationReceiver);
    }

    private void putMyLocationPin(LocationSample location) {
        PointSymbol pointSymbol = new PointImageSymbol(
                getString(R.string.viewer_self_icon_assets_path), 36, 36);
        putLocationPin(getString(R.string.viewer_my_location_name), location.getLocation(),
                pointSymbol);
    }

    private void putLocationPin(String id, PointGeometry pg, PointSymbol pointSymbol) {
        Entity point = mUsersLocationsLayer.getEntity(id);
        if (point == null) {
            point = new Point.Builder()
                    .setId(id).setGeometry(pg)
                    .setSymbol(pointSymbol)
                    .build();
            mUsersLocationsLayer.addEntity(point);
        } else {
            point.updateGeometry(pg);
        }
    }

    public void addMessageLocationPin(Message message) {
        Entity entity = mMessageLocationVM.addReceivedMessage(message);
        mReceivedLocationsLayer.addEntity(entity);
    }

    @Override
    public void displayMessageStatus() {
        Toast.makeText(getActivity(), "The image has been sent", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showProgress() {
        Toast.makeText(getActivity(), "Sending image", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showError(String message) {
        sLogger.e(message);
        Toast.makeText(getActivity(), "An error has occurred while sending image: " + message, Toast.LENGTH_LONG)
                .show();
    }

    private class UserLocationMessageHandler implements MessageBroadcastReceiver.NewMessageHandler {

        private final Logger mLogger = LoggerFactory.create(UserLocationMessageHandler.class);

        @Override
        public void onNewMessage(Message msg) {
            mLogger.v("Handling new User-Location Message from user with id " + msg.getSenderId());

            mUserLocationsVM.save((MessageUserLocation) msg);
            mUserLocationsVM.synchronizeToVectorLayer(mUsersLocationsLayer);
        }
    }
}
