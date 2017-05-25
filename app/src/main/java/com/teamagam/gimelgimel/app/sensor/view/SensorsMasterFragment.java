package com.teamagam.gimelgimel.app.sensor.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.RecyclerFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.sensor.viewModel.SensorsMasterViewModel;
import javax.inject.Inject;

public class SensorsMasterFragment extends RecyclerFragment<SensorsMasterViewModel> {

  @Inject
  SensorsMasterViewModel mViewModel;

  public SensorsMasterFragment() {

  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_sensors_master;
  }

  @Override
  protected SensorsMasterViewModel getSpecificViewModel() {
    return mViewModel;
  }

  @Override
  protected ViewDataBinding bindViewModel(View rootView) {
    com.teamagam.gimelgimel.databinding.FragmentSensorsMasterBinding binding =
        com.teamagam.gimelgimel.databinding.FragmentSensorsMasterBinding.bind(rootView);
    binding.setViewModel(mViewModel);
    return binding;
  }

  @Override
  protected int getRecyclerId() {
    return R.id.fragment_sensor_recycler;
  }
}
