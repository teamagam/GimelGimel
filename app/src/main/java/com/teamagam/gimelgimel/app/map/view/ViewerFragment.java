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
import com.teamagam.gimelgimel.app.map.esri.EsriGGMapView;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Viewer Fragment that handles all map events.
 */
public class ViewerFragment extends BaseFragment<GGApplication> {

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
        mMapViewModel.setMapView(mGGMapView);
        mMapViewModel.start();

        bind.setViewModel(mMapViewModel);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGGMapView.restoreState();
        ((EsriGGMapView) mGGMapView).unpause();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGGMapView.saveState();
        ((EsriGGMapView) mGGMapView).pause();
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

    public void lookAt(PointGeometryApp pga) {
        mGGMapView.lookAt(pga.getPointDomain());
    }
}
