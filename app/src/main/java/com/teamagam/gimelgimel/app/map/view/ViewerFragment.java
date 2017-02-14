package com.teamagam.gimelgimel.app.map.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.IMapView;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Viewer Fragment that handles all map events.
 */
public class ViewerFragment extends BaseFragment<GGApplication>
        implements IMapView {

    @BindView(R.id.gg_map_view)
    GGMapView mGGMapView;

    @Inject
    MapViewModel mMapViewModel;

    @Override
    @NotNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        com.teamagam.gimelgimel.databinding.FragmentViewerBinding bind = DataBindingUtil.bind(
                rootView);

        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
        mMapViewModel.setMapView(this);

        bind.setViewModel(mMapViewModel);

        mGGMapView.setGGMapGestureListener(mMapViewModel.getGestureListener());

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        mGGMapView.setOnReadyListener(new GGMap.OnReadyListener() {
            @Override
            public void onReady() {
                mMapViewModel.onMapReady();
            }
        });

        mMapViewModel.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapViewModel.stop();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_viewer;
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
    public void updateMapEntity(GeoEntityNotification geoEntityNotification) {
        mGGMapView.updateMapEntity(geoEntityNotification);
    }

    @Override
    public void showVectorLayer(VectorLayerPresentation vectorLayerPresentation) {
        mGGMapView.showVectorLayer(vectorLayerPresentation);
    }

    @Override
    public void hideVectorLayer(String vectorLayerId) {
        mGGMapView.hideVectorLayer(vectorLayerId);
    }
}
