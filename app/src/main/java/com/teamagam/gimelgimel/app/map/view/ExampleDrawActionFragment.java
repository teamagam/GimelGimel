package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.viewModel.BaseMapViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.BaseMapViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;

public class ExampleDrawActionFragment extends BaseDrawActionFragment<BaseMapViewModel> {


    @Inject
    BaseMapViewModelFactory mBaseMapViewModelFactory;

    private BaseMapViewModel mBaseMapViewModel;

    @BindView(R.id.example_draw_action_map_view)
    GGMapView mGGMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mApp.getApplicationComponent().inject(this);
        mBaseMapViewModel = mBaseMapViewModelFactory.create(mGGMapView);

        return view;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_example_draw_action;
    }

    @Override
    public void onPositiveButtonClick() {
    }

    @Override
    public String getPositiveButtonText() {
        return getResources().getString(R.string.draw_action_positive_button);
    }

    @Override
    protected BaseMapViewModel getSpecificViewModel() {
        return mBaseMapViewModel;
    }
}
