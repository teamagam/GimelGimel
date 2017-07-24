package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.viewModel.SendGeometryViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.SendGeometryViewModelFactory;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.databinding.FragmentSendGeometryBinding;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import javax.inject.Inject;

public class SendGeometryActionFragment extends DrawGeometryActionFragment<SendGeometryViewModel> {

  @Inject
  SendGeometryViewModelFactory mSendGeometryViewModelFactory;

  @BindView(R.id.send_geometry_mapview)
  GGMapView mGGMapView;

  private SendGeometryViewModel mViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);

    initializeViewModel();
    setupMapClickDelegation();

    return bind(view).getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mGGMapView.setOnMapGestureListener(null);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_send_geometry;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.send_geometry_toolbar_title);
  }

  @Override
  protected SendGeometryViewModel getSpecificViewModel() {
    return mViewModel;
  }

  @Override
  protected void onBorderStyleSelected(String borderStyle) {
    mViewModel.onBorderStyleSelected(borderStyle);
  }

  @Override
  protected void onColorSelected(boolean positiveResult, int color) {
    mViewModel.onColorSelected(positiveResult, color);
  }

  private void initializeViewModel() {
    mViewModel =
        mSendGeometryViewModelFactory.create(mGGMapView, this::notifyInvalidInput, this::pickColor,
            this::pickBorderStyle, this::finish);
    mViewModel.init();
  }

  private void notifyInvalidInput() {
    Snackbar.make(getView(), getString(R.string.send_geometry_invalid_input_message),
        Snackbar.LENGTH_SHORT).show();
  }

  private void setupMapClickDelegation() {
    mGGMapView.setOnMapGestureListener(new OnMapGestureListener() {
      @Override
      public void onLongPress(PointGeometry pointGeometry) {

      }

      @Override
      public void onTap(PointGeometry pointGeometry) {
        mViewModel.onLocationSelection(pointGeometry);
      }
    });
  }

  private FragmentSendGeometryBinding bind(View view) {
    FragmentSendGeometryBinding binding = FragmentSendGeometryBinding.bind(view);
    binding.setViewModel(mViewModel);
    return binding;
  }
}
