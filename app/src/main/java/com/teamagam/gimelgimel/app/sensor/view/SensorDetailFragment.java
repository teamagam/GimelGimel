package com.teamagam.gimelgimel.app.sensor.view;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.app.sensor.viewModel.SensorDetailsViewModel;
import com.teamagam.gimelgimel.databinding.FragmentSensorDetailBinding;


public class SensorDetailFragment extends BaseDataFragment<SensorDetailsViewModel> {

    private SensorDetailsViewModel mViewModel;

    public SensorDetailFragment() {
        // Required empty public constructor
        mViewModel = new SensorDetailsViewModel();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_sensor_detail;
    }


    @Override
    protected SensorDetailsViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentSensorDetailBinding bind =
                com.teamagam.gimelgimel.databinding.FragmentSensorDetailBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        return bind;
    }
}
