package com.teamagam.gimelgimel.app.view.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGImageSender;
import com.teamagam.gimelgimel.app.network.services.IImageSender;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.utils.ImageUtil;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendMessageDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.ShowMessageDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.view.viewer.data.EntitiesHelperUtils;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.view.viewer.gestures.MapGestureDetector;
import com.teamagam.gimelgimel.app.view.viewer.gestures.SimpleOnMapGestureListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements
        SendGeographicMessageDialog.SendGeographicMessageDialogInterface,
        ShowMessageDialogFragment.ShowMessageDialogFragmentInterface, OnGGMapReadyListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final String IMAGE_URI_KEY = "IMAGE_CAMERA_URI";

    private VectorLayer mSentLocationsLayer;
    private VectorLayer mUsersLocationsLayer;

    private OnFragmentInteractionListener mListener;

    private GGMapView mGGMapView;
    private MessageBroadcastReceiver mUserLocationReceiver;
    private BroadcastReceiver mLocationReceiver;
    private IImageSender mImageSender;

    private Uri mImageUri;
    private boolean mIsRestored;

    @Override
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, rootView);

        mSentLocationsLayer = new VectorLayer("vl2");
        mUsersLocationsLayer = new VectorLayer("vlUsersLocation");

        mGGMapView = (GGMapView) rootView.findViewById(R.id.gg_map_view);
        mImageSender = new GGImageSender();

        MapGestureDetector mgd = new MapGestureDetector(mGGMapView,
                new SimpleOnMapGestureListener() {
                    @Override
                    public void onLongPress(PointGeometry pointGeometry) {
                        /** create send geo message dialog **/
                        sLogger.userInteraction("Long tap on map");
                        onCreateGeographicMessage(pointGeometry);
                    }
                });
        mgd.startDetecting();

        mUserLocationReceiver = new MessageBroadcastReceiver(
                new MessageBroadcastReceiver.NewMessageHandler() {
                    @Override
                    public void onNewMessage(Message msg) {
                        String id = msg.getSenderId();
                        LocationSample loc = (LocationSample) msg.getContent();
                        putUserLocationPin(id, loc.getLocation());
                    }
                }, Message.USER_LOCATION);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void drawPin(PointGeometry pointGeometry) {
        if (pointGeometry == null) {
            throw new IllegalArgumentException("given pointGeometry is null!");
        }

        addPinPoint(pointGeometry, mSentLocationsLayer);
    }

    @Override
    public void goToLocation(PointGeometry pointGeometry) {
        mGGMapView.zoomTo(pointGeometry);
    }

    public GGMap getGGMap() {
        return mGGMapView;
    }

    @Override
    public void onGGMapViewReady() {
        if(!mIsRestored) {
            setInitialMapExtent();
        }

        mGGMapView.addLayer(mSentLocationsLayer);
        mGGMapView.addLayer(mUsersLocationsLayer);

        registerForLocationUpdates();
    }

    private boolean isImageCaptured(int resultCode) {
        return resultCode == Activity.RESULT_OK && mImageUri != null;
    }

    private void sendCapturedImageToServer() {
        LocationSample imageLocation = LocationFetcher.getInstance(
                getActivity()).getLastKnownLocation();
        long imageTime = new Date().getTime();
        PointGeometry loc = null;
        if (imageLocation != null) {
            loc = imageLocation.getLocation();
        }
        mImageSender.sendImage(getActivity(), mImageUri, imageTime, loc);
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

    private void putUserLocationPin(String id, PointGeometry pg) {
        PointSymbol pointSymbol = new PointTextSymbol(EntitiesHelperUtils.getRandomCssColorStirng(),
                id, 48);
        putLocationPin(id, pg, pointSymbol);
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

    private void addPinPoint(PointGeometry pointGeometry, VectorLayer vectorLayer) {

        //Todo: use symbol interface
        PointImageSymbol pointSymbol = new PointImageSymbol(
                "Cesium/Assets/Textures/maki/marker.png", 36,
                36);
        final Point point = new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(pointSymbol)
                .build();
        if (mGGMapView.getLayer(vectorLayer.getId()) == null) {
            mGGMapView.addLayer(vectorLayer);
        }

        vectorLayer.addEntity(point);
    }

    private void onCreateGeographicMessage(PointGeometry pointGeometry) {
        SendGeographicMessageDialog sendGeographicMessageDialogFragment =
                SendGeographicMessageDialog.newInstance(pointGeometry, this);

        sendGeographicMessageDialogFragment.show(getFragmentManager(), "sendCoordinatesDialog");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
