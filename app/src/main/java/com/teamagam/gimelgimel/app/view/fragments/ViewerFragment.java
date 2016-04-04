package com.teamagam.gimelgimel.app.view.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.utils.IdCreatorUtil;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.GoToDialogFragment;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.ShowMessageDialogFragment;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.data.EntitiesHelperUtils;
import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.KMLLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.view.viewer.gestures.MapGestureDetector;
import com.teamagam.gimelgimel.app.view.viewer.gestures.SimpleOnMapGestureListener;

import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements
        View.OnClickListener,
        SendGeographicMessageDialog.SendGeographicMessageDialogInterface,
        ShowMessageDialogFragment.ShowMessageDialogFragmentInterface,
        GoToDialogFragment.GoToDialogFragmentInterface {

    private VectorLayer mVL;
    private VectorLayer mSentLocationsLayer;
    private KMLLayer mKL;
    //

    private OnFragmentInteractionListener mListener;

    private GGMapView mGGMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mVL = new VectorLayer("vl");
        mSentLocationsLayer = new VectorLayer("vl2");
        mKL = new KMLLayer("kl", "SampleData/kml/facilities.kml");

        mGGMapView = (GGMapView) rootView.findViewById(R.id.gg_map_view);

        Button addVectorLayerButton = (Button) rootView.findViewById(
                R.id.fab_custom_layer_creation_test);
        Button updateVectorLayerButton = (Button) rootView.findViewById(
                R.id.fab_custom_layer_update_test);
        Button kmlLayerTestButton = (Button) rootView.findViewById(
                R.id.fab_kml_layer_test);
        Button removeLayersButton = (Button) rootView.findViewById(
                R.id.fab_remove_layers_test);
        Button removeEntityButton = (Button) rootView.findViewById(R.id.fab_remove_entity_test);

        Button goToButton = (Button) rootView.findViewById(R.id.goto_button);
        Button displayCenterLocationButton = (Button) rootView.findViewById(
                R.id.center_display_button);
        Button displayTouchedLocationButton = (Button) rootView.findViewById(
                R.id.touched_display_button);

        setOnClickListener(this, goToButton, displayCenterLocationButton,
                displayTouchedLocationButton, addVectorLayerButton, updateVectorLayerButton,
                kmlLayerTestButton, removeEntityButton, removeLayersButton);

        MapGestureDetector mgd = new MapGestureDetector(mGGMapView,
                new SimpleOnMapGestureListener() {
                    @Override
                    public void onLongPress(PointGeometry pointGeometry) {
                        /** create send geo message dialog **/
                        onCreateGeographicMessage(pointGeometry);
                    }
                });
        mgd.startDetecting();

        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cesium;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goto_button:
                DialogFragment newFragment = new GoToDialogFragment();
                newFragment.setTargetFragment(ViewerFragment.this, 0);
                newFragment.show(getActivity().getFragmentManager(), "dialog");
                break;
            case R.id.center_display_button:
                mGGMapView.readAsyncCenterPosition(new ValueCallback<PointGeometry>() {
                    @Override
                    public void onReceiveValue(PointGeometry point) {
                        Toast.makeText(getActivity(),
                                String.format("N:%.4f E:%.4f", point.latitude,
                                        point.longitude), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.touched_display_button:
                PointGeometry pg = mGGMapView.getLastTouchedLocation();
                String toastText;
                if (pg == null) {
                    toastText = "No location selected";
                } else {
                    toastText = String.format("Lat/Long: %.2f/%.2f",
                            pg.latitude, pg.longitude);
                }
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_custom_layer_creation_test:
                onCreateLayerClick();
                break;
            case R.id.fab_custom_layer_update_test:
                onUpdateVectorLayerClick();
                break;
            case R.id.fab_kml_layer_test:
                onLoadKmlLayerClick();
                break;
            case R.id.fab_remove_layers_test:
                onRemoveLayersClick();
                break;
            case R.id.fab_remove_entity_test:
                onRemoveEntityClick();
                break;
            default:
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public void onCreateGeographicMessage(PointGeometry pointGeometry) {
        SendGeographicMessageDialog sendGeographicMessageDialogFragment =
                SendGeographicMessageDialog.newInstance(pointGeometry, this);

        sendGeographicMessageDialogFragment.show(getFragmentManager(), "sendCoordinatesDialog");
    }

    private void addPinPoint(PointGeometry pointGeometry, VectorLayer vectorLayer) {
        String id = IdCreatorUtil.getId();

        //Todo: use symbol interface
        PointImageSymbol pointSymbol = new PointImageSymbol(
                "Cesium/Assets/Textures/maki/marker.png", 36,
                36);
        final Point point = new Point(id, pointGeometry, pointSymbol);
        if (mGGMapView.getLayer(vectorLayer.getId()) == null) {
            mGGMapView.addLayer(vectorLayer);
        }

        vectorLayer.addEntity(point);
    }

    private void setOnClickListener(View.OnClickListener listener, View... views) {
        for (View v : views) {
            v.setOnClickListener(listener);
        }
    }

    private void onCreateLayerClick() {
        if (mGGMapView.getLayer(mVL.getId()) == null) {
            mGGMapView.addLayer(mVL);
        }

        //Generate a point around given lat/lng values and epsilon
        PointGeometry pointGeometry = EntitiesHelperUtils.generateRandomLocation(32.2, 34.8, 1);
        PointSymbol pointSymbol = EntitiesHelperUtils.generateRandomPointSymbol();
        Point p = new Point(IdCreatorUtil.getId(), pointGeometry, pointSymbol);
        mVL.addEntity(p);

        //Generate a random polyline
        MultiPointGeometry polylineMpg = EntitiesHelperUtils.generateRandomLocations(32.2, 34.8,
                1);
        PolylineSymbol polylineSymbol = EntitiesHelperUtils.generateRandomPolylineSymbol();
        Polyline pl = new Polyline(IdCreatorUtil.getId(), polylineMpg, polylineSymbol);
        mVL.addEntity(pl);

        //Generate random polygon
        MultiPointGeometry polygonMpg = EntitiesHelperUtils.generateRandomLocations(32.2, 34.8, 1);
        PolygonSymbol polygonSymbol = EntitiesHelperUtils.generateRandomPolygonSymbol();
        Polygon polygon = new Polygon(IdCreatorUtil.getId(), polygonMpg, polygonSymbol);
        mVL.addEntity(polygon);
    }

    private void onRemoveEntityClick() {
        Entity[] entities = mVL.getEntities().toArray(new Entity[0]);

        if (entities.length == 0) {
            Toast.makeText(getActivity(), "No entities to remove from vector layer",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Entity randEntity = entities[((int) Math.floor(Math.random() * entities.length))];

        mVL.removeEntity(randEntity.getId());
    }

    private void onUpdateVectorLayerClick() {
        if (mGGMapView.getLayer(mVL.getId()) == null) {
            Toast.makeText(getActivity(), "First, create a layer", Toast.LENGTH_SHORT).show();
            return;
        }

        Collection<Entity> vEntities = mVL.getEntities();

        EntitiesHelperUtils.randomlyUpdateEntities(vEntities);
    }

    private void onLoadKmlLayerClick() {
        if (mGGMapView.getLayer(mKL.getId()) == null) {
            mGGMapView.addLayer(mKL);
        }
    }

    private void onRemoveLayersClick() {
        Collection<GGLayer> layers = mGGMapView.getLayers();

        //Avoids concurrent modification while iterating (cannot foreach)
        GGLayer[] layerArray = layers.toArray(new GGLayer[0]);
        for (int i = 0; i < layerArray.length; i++) {
            mGGMapView.removeLayer(layerArray[i].getId());
        }

        Entity[] entities = mVL.getEntities().toArray(new Entity[0]);
        for (int i = 0; i < entities.length; i++) {
            mVL.removeEntity(entities[i].getId());
        }
    }


    @Override
    public void drawPin(PointGeometry pointGeometry) {
        addPinPoint(pointGeometry, mSentLocationsLayer);
    }

    @Override
    public void goToLocation(PointGeometry pointGeometry) {
        //TODO: avoid hack
        //Hack: preserve current altitude if given pointGeometry altitude is zero
        if (pointGeometry.altitude == 0) {
            pointGeometry.altitude = -1;
        }
        mGGMapView.zoomTo(pointGeometry);
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
