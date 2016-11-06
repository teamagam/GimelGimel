package com.teamagam.gimelgimel.app.map.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.map.cesium.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.IMapView;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.GGMapGestureListener;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.MainActivity;
import com.teamagam.gimelgimel.app.view.fragments.BaseFragment;
import com.teamagam.gimelgimel.databinding.FragmentCesiumBinding;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Viewer Fragment that handles all map events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> implements OnGGMapReadyListener,
        IMapView {

    @BindView(R.id.gg_map_view)
    GGMapView mGGMapView;

    @Inject
    MapViewModel mMapViewModel;

    @Override
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        FragmentCesiumBinding bind = DataBindingUtil.bind(rootView);

        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
        mMapViewModel.setMapView(this);

        bind.setViewModel(mMapViewModel);

        mGGMapView.setGGMapGestureListener(new GGMapGestureListener(mGGMapView, this));

        if (savedInstanceState != null) {
            mGGMapView.restoreViewState(savedInstanceState);
        }
        secureGGMapViewInitialization();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapViewModel.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapViewModel.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapViewModel.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapViewModel.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mGGMapView.saveViewState(outState);
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
    public void onDetach() {
        mMapViewModel.destroy();
        super.onDetach();
    }

    @Override
    public void lookAt(PointGeometryApp pointGeometry) {
        mGGMapView.lookAt(pointGeometry);
    }

    @Override
    public void lookAt(PointGeometryApp pointGeometry, float newHeight) {
        mGGMapView.lookAt(pointGeometry, newHeight);
    }

    @Override
    public void setCameraPosition(ViewerCamera viewerCamera) {
        mGGMapView.setCameraPosition(viewerCamera);
    }


    @Override
    public void addLayer(VectorLayer vectorLayer) {
        mGGMapView.addLayer(vectorLayer);
    }

    @Override
    public void openSendGeoDialog(PointGeometryApp pointGeometry) {
        SendGeographicMessageDialog.newInstance(pointGeometry, this)
                .show(this.getFragmentManager(), "sendCoordinatesDialog");
    }

    public GGMap getGGMap() {
        return mGGMapView;
    }

    @Override
    public void onGGMapViewReady() {
        mMapViewModel.mapReady();
    }
}
