package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.model.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageMapEntitiesViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGImageSender;
import com.teamagam.gimelgimel.app.network.services.IImageSender;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendMessageDialogFragment;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import java.net.URI;

import javax.inject.Inject;

/**
 * View Model that handles map related callbacks from user in {@link ViewerFragment}.
 * Also handles it's view {@link GGMapView}.
 * <p>
 * Controls communication between views and models of the presentation
 * layer.
 */
@PerFragment
public class MapViewModel {
//implements SendGeographicMessageDialog.SendGeographicMessageDialogInterface

    IMapView mMapView;

    private VectorLayer mSentLocationsLayer;
    private VectorLayer mUsersLocationsLayer;
    private VectorLayer mReceivedLocationsLayer;

    private MessageBroadcastReceiver mUserLocationReceiver;
    private BroadcastReceiver mLocationReceiver;
    private IImageSender mImageSender;

    private Handler mHandler;
    private Runnable mPeriodicalUserLocationsRefreshRunnable;


    //injects
    @Inject
    MessageMapEntitiesViewModel mMessageLocationVM;

    private UsersLocationViewModel mUserLocationsVM;

    private final Activity mActivity;

    Context mContext;

    //logger
    private Logger sLogger = LoggerFactory.create(getClass());

    @Inject
    public MapViewModel(Context context, Activity activity, UsersLocationViewModel userLocationVM) {
        mContext = context;
        mActivity = activity;
        mUserLocationsVM = userLocationVM;

        //user location handling
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

        mUserLocationReceiver = new MessageBroadcastReceiver(
                new UserLocationMessageHandler(), Message.USER_LOCATION);


        //location receiver
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

        //image sending
        mImageSender = new GGImageSender();

        //vector layers
        mSentLocationsLayer = new VectorLayer("vl2");
        mReceivedLocationsLayer = new VectorLayer("vlReceivedLocation");
        mUsersLocationsLayer = new VectorLayer("vlUsersLocation");


    }

    public void setMapView(IMapView mapView) {
        mMapView = mapView;
    }

    public void resume() {
        startPeriodicalUserLocationsRefresh();
    }

    public void pause() {
        stopPeriodicalUserLocationRefresh();
    }

    public void destroy() {
        if (mUserLocationReceiver != null) {
            MessageBroadcastReceiver.unregisterReceiver(mContext, mUserLocationReceiver);
        }
        if (mLocationReceiver != null) {
            LocationFetcher.getInstance(mContext).unregisterReceiver(mLocationReceiver);
        }
    }

    public void sendMessageClicked() {
        sLogger.userInteraction("Send message button clicked");
        new SendMessageDialogFragment()
                .show(mActivity.getFragmentManager(), "sendMessageDialog");
    }

    public void sendImageClicked() {
        sLogger.userInteraction("Start camera activity button clicked");
        mMapView.takePicture();
    }

    public void sendCapturedImage(boolean isImageCaptured, Uri uri) {
//         LocationSample imageLocation = LocationFetcher.getInstance(
//                    getActivity()).getLastKnownLocation();
//            long imageTime = new Date().getTime();
//            PointGeometry loc = null;
//            if (imageLocation != null) {
//                loc = imageLocation.getLocation();
//            }
//            mImageSender.sendImage(getActivity(), mImageUri, imageTime, loc);
//
//        if (isImageCaptured(resultCode)) {
//            sLogger.userInteraction("Sending camera activity result");
//            mMapViewModel.sendCapturedImage(true, mImageUri);
//        } else {
//            sLogger.userInteraction("Camera activity returned with no captured image");
//        }
        if (isImageCaptured) {
            mImageSender.sendImage(mContext, uri, 1, PointGeometry.DEFAULT_POINT);
        }
    }

    public void zoomToLastKnownLocation() {
        sLogger.userInteraction("Locate me button clicked");

        LocationSample lastKnownLocation = LocationFetcher.getInstance(
                mContext).getLastKnownLocation();

        if (lastKnownLocation == null) {
            Toast.makeText(mContext, R.string.locate_me_fab_no_known_location,
                    Toast.LENGTH_SHORT).show();
        } else {
            PointGeometry location = lastKnownLocation.getLocation();

            location.altitude = Constants.LOCATE_ME_BUTTON_ALTITUDE_METERS;
            mMapView.goToLocation(location);
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

    private boolean isImageCaptured(int resultCode, URI imageUri) {
        return resultCode == Activity.RESULT_OK && imageUri != null;
    }

    private void putMyLocationPin(LocationSample location) {
        PointSymbol pointSymbol = new PointImageSymbol(
                mContext.getString(R.string.viewer_self_icon_assets_path), 36, 36);
        putLocationPin(mContext.getString(R.string.viewer_my_location_name), location.getLocation(),
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

    public void mapReady() {
        mMapView.addLayer(mSentLocationsLayer);
        mMapView.addLayer(mUsersLocationsLayer);
        mMapView.addLayer(mReceivedLocationsLayer);

        mUserLocationsVM.synchronizeToVectorLayer(mUsersLocationsLayer);

        registerForLocationUpdates();
    }

    private void registerForLocationUpdates() {
        //Register for new incoming users location messages
        MessageBroadcastReceiver.registerReceiver(mContext, mUserLocationReceiver);

        //Register for local location messages
        LocationFetcher.getInstance(mContext).registerReceiver(mLocationReceiver);
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
