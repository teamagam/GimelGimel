package com.teamagam.gimelgimel.app.map.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseViewModelFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.esri.EsriGGMapView;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import javax.inject.Inject;

/**
 * Viewer Fragment that handles all map events.
 */
public class ViewerFragment extends BaseViewModelFragment<ViewerViewModel>
    implements ViewerCameraController {

  @BindView(R.id.locate_me_fab)
  FloatingActionButton mLocateMeFab;
  @BindView(R.id.gg_map_view)
  GGMapView mGGMapView;

  @Inject
  ViewerViewModelFactory mMapViewModelFactory;

  private ViewerViewModel mViewerViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    com.teamagam.gimelgimel.databinding.FragmentViewerBinding bind = DataBindingUtil.bind(rootView);

    ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    mViewerViewModel = mMapViewModelFactory.create(mGGMapView);
    mViewerViewModel.setView(this);
    mViewerViewModel.init();

    bind.setViewModel(mViewerViewModel);
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
  public void setViewerCamera(Geometry geometry) {
    mViewerViewModel.setViewerCamera(geometry);
  }

  @Override
  protected ViewerViewModel getSpecificViewModel() {
    return mViewerViewModel;
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_viewer;
  }

  public void informUnknownLocation() {
    Snackbar.make(getView(), R.string.unknown_location, Snackbar.LENGTH_SHORT).show();
  }
}
