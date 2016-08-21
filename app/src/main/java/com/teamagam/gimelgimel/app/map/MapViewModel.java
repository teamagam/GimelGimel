package com.teamagam.gimelgimel.app.map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageMapEntitiesViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGImageSender;
import com.teamagam.gimelgimel.app.network.services.IImageSender;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;

/**
 * View Model that handles all callbacks from user in {@link ViewerFragment}.
 * Also handles it's view {@link GGMapView}.
 * /**
 * Controls communication between views and models of the presentation
 * layer.
 */
@PerFragment
public class MapViewModel {

    @Inject
//    @BindView(R.id.gg_map_view)
    MapView mMapView;

    private VectorLayer mSentLocationsLayer;
    private VectorLayer mUsersLocationsLayer;
    private VectorLayer mReceivedLocationsLayer;

    private UsersLocationViewModel mUserLocationsVM;

    private MessageBroadcastReceiver mUserLocationReceiver;
    private BroadcastReceiver mLocationReceiver;
    private IImageSender mImageSender;

    private Uri mImageUri;
    private boolean mIsRestored;
    private Handler mHandler;
    private Runnable mPeriodicalUserLocationsRefreshRunnable;
    private MessageMapEntitiesViewModel mMessageLocationVM;

    private Logger sLogger = LoggerFactory.create(((Object) this).getClass());

    public void MapViewModel(GGApplication mApp) {
        mMessageLocationVM = mApp.getMessageMapEntitiesViewModel();
        mUserLocationsVM = mApp.getUserLocationViewModel();
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

        mSentLocationsLayer = new VectorLayer("vl2");
        mReceivedLocationsLayer = new VectorLayer("vlReceivedLocation");
        mUsersLocationsLayer = new VectorLayer("vlUsersLocation");

        mImageSender = new GGImageSender();

//vf        mGGMapView.setGGMapGestureListener(new GGMapGestureListener(this, mGGMapView));

//        mUserLocationReceiver = new MessageBroadcastReceiver(
//                new UserLocationMessageHandler(), Message.USER_LOCATION);

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

//vf        if (savedInstanceState != null) {
//            mGGMapView.restoreViewState(savedInstanceState);
//            mIsRestored = true;
//        } else {
//            mIsRestored = false;
//        }

    }

    //todo: think
    public void resume() {
        startPeriodicalUserLocationsRefresh();
    }

    //todo: think
    public void pause() {
        stopPeriodicalUserLocationRefresh();
    }

    public void sendMessageFAB() {
        sLogger.userInteraction("Send message button clicked");
//        mMapView.openSendMessageDialog();
    }

    public void startCameraActivity() {
        sLogger.userInteraction("Start camera activity button clicked");
//        mMapView.takePicture();
    }


    public void sendImage(boolean isImageCaptured, Uri uri){
        sendCapturedImageToServer(uri);
    }

    Context getActivity(){
        return null;
    }

//    @OnClick(R.id.locate_me_fab)
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
//            mMapView.zoomTo(location);
        }
    }

    public void destroy(){
        if (mUserLocationReceiver != null) {
            MessageBroadcastReceiver.unregisterReceiver(getActivity(), mUserLocationReceiver);
        }
        if (mLocationReceiver != null) {
            LocationFetcher.getInstance(getActivity()).unregisterReceiver(mLocationReceiver);
        }
    }


    public void drawSentPin(Message message) {
        Entity entity = mMessageLocationVM.addSentMessage(message);
        mSentLocationsLayer.addEntity(entity);
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

    private void sendCapturedImageToServer(Uri uri) {
//        mImageSender.sendImage(uri);
    }

    private void putMyLocationPin(LocationSample location) {
//        PointSymbol pointSymbol = new PointImageSymbol(
//                getString(R.string.viewer_self_icon_assets_path), 36, 36);
//        putLocationPin(getString(R.string.viewer_my_location_name), location.getLocation(),
//                pointSymbol);
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

}
