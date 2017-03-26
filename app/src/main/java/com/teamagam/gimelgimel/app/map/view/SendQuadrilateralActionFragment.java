package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.HorizontalLongLatPicker;
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

    @BindView(R.id.send_quadrilateral_action_long_lat1)
    HorizontalLongLatPicker mPicker1;

    @BindView(R.id.send_quadrilateral_action_long_lat2)
    HorizontalLongLatPicker mPicker2;

    @BindView(R.id.send_quadrilateral_action_long_lat3)
    HorizontalLongLatPicker mPicker3;

    @BindView(R.id.send_quadrilateral_action_long_lat4)
    HorizontalLongLatPicker mPicker4;

    @BindView(R.id.send_quadrilateral_description_edit_text)
    EditText mDescriptionEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mApp.getApplicationComponent().inject(this);
        mViewModel = mSendQuadrilateralActionViewModelFactory.create(
                mGGMapView,
                this,
                new HorizontalLongLatPicker[]{mPicker1, mPicker2, mPicker3, mPicker4});
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
    public void onShowButtonClicked() {
        mViewModel.onShowButtonClicked();
    }

    public void showInvalidInput() {
        Snackbar.make(mPicker1, R.string.send_quadrilateral_invalid_input_message,
                Snackbar.LENGTH_SHORT).show();
    }

    public String getDescription() {
        return mDescriptionEditText.getText().toString();
    }

    public void finish() {
        getActivity().finish();
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
