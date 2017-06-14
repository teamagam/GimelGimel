package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.viewModel.MeasureActionViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.MeasureActionViewModelFactory;
import com.teamagam.gimelgimel.databinding.FragmentMeasureActionBinding;
import javax.inject.Inject;

public class MeasureActionFragment extends BaseDrawActionFragment<MeasureActionViewModel> {

  @Inject
  MeasureActionViewModelFactory mMeasureActionViewModelFactory;

  @BindView(R.id.measure_map_view)
  GGMapView mGGMapView;

  private MeasureActionViewModel mViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);
    mViewModel = mMeasureActionViewModelFactory.create(mGGMapView);
    mViewModel.init();

    FragmentMeasureActionBinding bind = FragmentMeasureActionBinding.bind(view);
    bind.setViewModel(mViewModel);

    return bind.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mViewModel.destroy();
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_measure_action;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.measure_distance_title);
  }

  @Override
  protected MeasureActionViewModel getSpecificViewModel() {
    return mViewModel;
  }
}
