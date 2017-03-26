package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.viewModel.ExampleDrawActionViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.ExampleDrawActionViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;

public class ExampleDrawActionFragment extends BaseDrawActionFragment<ExampleDrawActionViewModel> {

    @Inject
    ExampleDrawActionViewModelFactory mExampleDrawActionViewModelFactory;

    private ExampleDrawActionViewModel mExampleDrawActionViewModel;

    @BindView(R.id.example_draw_action_map_view)
    GGMapView mGGMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mApp.getApplicationComponent().inject(this);
        mExampleDrawActionViewModel = mExampleDrawActionViewModelFactory.create(mGGMapView);
        mExampleDrawActionViewModel.init();

        return view;
    }

    @Override
    public void onPositiveButtonClick() {
    }

    @Override
    public String getPositiveButtonText() {
        return getResources().getString(R.string.draw_action_positive_button);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_example_draw_action;
    }

    @Override
    protected ExampleDrawActionViewModel getSpecificViewModel() {
        return mExampleDrawActionViewModel;
    }
}
