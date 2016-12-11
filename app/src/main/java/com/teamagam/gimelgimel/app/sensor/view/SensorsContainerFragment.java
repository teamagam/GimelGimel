package com.teamagam.gimelgimel.app.sensor.view;


import android.app.Fragment;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.sensor.viewModel.SensorsContainerViewModel;
import com.teamagam.gimelgimel.databinding.FragmentSensorContainerBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorsContainerFragment extends BaseDataFragment<SensorsContainerViewModel> {

    private SensorsContainerViewModel mViewModel;


    public SensorsContainerFragment() {
        mViewModel = new SensorsContainerViewModel();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_sensor_container;
    }

    @Override
    protected SensorsContainerViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentSensorContainerBinding binding = FragmentSensorContainerBinding.bind(rootView);
        binding.setViewModel(mViewModel);
        return binding;
    }
}
