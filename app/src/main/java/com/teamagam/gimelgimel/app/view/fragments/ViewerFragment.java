package com.teamagam.gimelgimel.app.view.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements
        SendGeographicMessageDialog.SendGeographicMessageDialogInterface,
        ShowMessageDialogFragment.ShowMessageDialogFragmentInterface, OnGGMapReadyListener {

    private VectorLayer mSentLocationsLayer;
    private VectorLayer mUsersLocationsLayer;

    private OnFragmentInteractionListener mListener;

    private GGMapView mGGMapView;
    private MessageBroadcastReceiver mUserLocationReceiver;
    private BroadcastReceiver mLocationReceiver;
    private LocationFetcher mLocationFetcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mSentLocationsLayer = new VectorLayer("vl2");
        mUsersLocationsLayer = new VectorLayer("vlUsersLocation");

        mGGMapView = (GGMapView) rootView.findViewById(R.id.gg_map_view);

        MapGestureDetector mgd = new MapGestureDetector(mGGMapView,
                new SimpleOnMapGestureListener() {
                    @Override
                    public void onLongPress(PointGeometry pointGeometry) {
                        /** create send geo message dialog **/
                        onCreateGeographicMessage(pointGeometry);
                    }
                });
        mgd.startDetecting();

        FloatingActionButton messageFab = (FloatingActionButton) rootView.findViewById(
                R.id.message_fab);
        messageFab.setBackgroundDrawable(getActivity().getDrawable(R.drawable.ic_message));
        messageFab.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        DialogFragment sendMessageDialogFragment = new SendMessageDialogFragment();
                        sendMessageDialogFragment.show(
                                getFragmentManager(),
                                "sendMessageDialog");
                    }
                }
        );

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
                if (intent.getExtras().containsKey(LocationManager.KEY_LOCATION_CHANGED)) {
                    putMyLocationPin(LocationFetcher.getLocationSample(intent));
                }
            }
        };

        mLocationFetcher = LocationFetcher.getInstance(getActivity());

        secureGGMapViewInitialization();

        return rootView;
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
        if (mLocationFetcher != null && mLocationReceiver != null) {
            mLocationFetcher.unregisterReceiver(mLocationReceiver);
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
        setInitialMapExtent();

        mGGMapView.addLayer(mSentLocationsLayer);
        mGGMapView.addLayer(mUsersLocationsLayer);

        registerForLocationUpdates();
    }

    /**
     * Sets GGMapView extent to configured bounding box values
     */
    private void setInitialMapExtent() {
        float east = parseStringResource(R.string.map_view_initial_bounding_box_east);
        float west = parseStringResource(R.string.map_view_initial_bounding_box_west);
        float north = parseStringResource(R.string.map_view_initial_bounding_box_north);
        float south = parseStringResource(R.string.map_view_initial_bounding_box_south);
        mGGMapView.setExtent(west, south, east, north);
    }

    private float parseStringResource(@StringRes int stringResourceId) {
        return Float.parseFloat(getResources().getString(stringResourceId));
    }

    private void registerForLocationUpdates() {
        MessageBroadcastReceiver.registerReceiver(getActivity(), mUserLocationReceiver);

        mLocationFetcher.registerReceiver(mLocationReceiver);
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
