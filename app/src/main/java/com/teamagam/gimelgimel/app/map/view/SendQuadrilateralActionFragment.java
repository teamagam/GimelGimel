package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.viewModel.SendQuadrilateralActionViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.SendQuadrilateralActionViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SendQuadrilateralActionFragment extends
        BaseDrawActionFragment<SendQuadrilateralActionViewModel> {

    @Inject
    SendQuadrilateralActionViewModelFactory mSendQuadrilateralActionViewModelFactory;

    private SendQuadrilateralActionViewModel mViewModel;

    @BindView(R.id.send_quadrilateral_action_map_view)
    GGMapView mGGMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mApp.getApplicationComponent().inject(this);
        mViewModel = mSendQuadrilateralActionViewModelFactory.create(
                mGGMapView);
        mViewModel.init();

        return view;
    }

    @Override
    public void onPositiveButtonClick() {
        mViewModel.onPositiveButtonClick();
    }

    @Override
    public String getPositiveButtonText() {
        return getResources().getString(R.string.draw_action_positive_button);
    }

    @OnClick(R.id.send_quadrilateral_action_show_button)
    public void onShowButtonClicked(){
        mViewModel.onShowButtonClicked();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_send_quadrilateral;
    }

    @Override
    protected SendQuadrilateralActionViewModel getSpecificViewModel() {
        return mViewModel;
    }
}
