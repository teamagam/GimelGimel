package com.teamagam.gimelgimel.app.sensor.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseDataBindingFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.sensor.viewModel.SensorDetailsViewModel;
import com.teamagam.gimelgimel.databinding.FragmentSensorDetailBinding;

import javax.inject.Inject;


public class SensorDetailFragment extends BaseDataBindingFragment<SensorDetailsViewModel> {

    @Inject
    SensorDetailsViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
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
