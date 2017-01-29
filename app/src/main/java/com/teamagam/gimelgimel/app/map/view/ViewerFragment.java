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
import com.teamagam.gimelgimel.app.map.cesium.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.IMapView;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;
import com.teamagam.gimelgimel.databinding.FragmentCesiumBinding;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;

/**
 * Viewer Fragment that handles all map events.
 */
public class ViewerFragment extends BaseFragment<GGApplication>
        implements OnGGMapReadyListener, IMapView {

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

        mGGMapView.setGGMapGestureListener(mMapViewModel.getGestureListener());
        mGGMapView.setOnEntityClickedListener(mMapViewModel);

        secureGGMapViewInitialization();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapViewModel.stop();
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
    public void addLayer(String layerId) {
        mGGMapView.addLayer(layerId);
    }

    @Override
    public Observable<ViewerCamera> getViewerCameraObservable() {
        return mGGMapView.getViewerCameraObservable();
    }

    @Override
    public void updateMapEntity(EntityUpdateEventArgs entityUpdateEventArgs) {
        mGGMapView.updateMapEntity(entityUpdateEventArgs);
    }

    @Override
    public void showVectorLayer(VectorLayerPresentation vectorLayerPresentation) {
        mGGMapView.showVectorLayer(vectorLayerPresentation);
    }

    @Override
    public void hideVectorLayer(String vectorLayerId) {
        mGGMapView.hideVectorLayer(vectorLayerId);
    }

    public GGMap getGGMap() {
        return mGGMapView;
    }

    @Override
    public void onGGMapViewReady() {
        mMapViewModel.mapReady();
    }
}
