package com.teamagam.gimelgimel.app.map.actions.send.quadrilateral;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.LatLongPicker;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.domain.utils.TextUtils;
import javax.inject.Inject;

public class SendQuadrilateralActionFragment
    extends BaseDrawActionFragment<SendQuadrilateralActionViewModel> {

  @Inject
  SendQuadrilateralActionViewModelFactory mSendQuadrilateralActionViewModelFactory;
  @BindView(R.id.send_quadrilateral_action_map_view)
  GGMapView mGGMapView;
  @BindView(R.id.send_quadrilateral_action_lat_long1)
  LatLongPicker mPicker1;
  @BindView(R.id.send_quadrilateral_action_lat_long2)
  LatLongPicker mPicker2;
  @BindView(R.id.send_quadrilateral_action_lat_long3)
  LatLongPicker mPicker3;
  @BindView(R.id.send_quadrilateral_action_lat_long4)
  LatLongPicker mPicker4;
  @BindView(R.id.send_quadrilateral_description_edit_text)
  EditText mDescriptionEditText;
  private SendQuadrilateralActionViewModel mViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    setupFocusOrder();

    mApp.getApplicationComponent().inject(this);
    mViewModel = mSendQuadrilateralActionViewModelFactory.create(mGGMapView, this,
        new LatLongPicker[] { mPicker1, mPicker2, mPicker3, mPicker4 });
    mViewModel.init();

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mViewModel.destroy();
  }

  @OnClick(R.id.send_quadrilateral_action_show_button)
  public void onShowButtonClick() {
    mViewModel.onShowButtonClick();
  }

  @OnClick(R.id.fab_action_send_quadrilateral)
  public void onSendClick() {
    mViewModel.onSendClick();
  }

  @OnClick(R.id.restore_history_image_view)
  public void onRestoreValuesClick() {
    mViewModel.onRestoreValuesClick();
  }

  public void showInvalidInput() {
    Snackbar.make(mPicker1, R.string.send_quadrilateral_invalid_input_message,
        Snackbar.LENGTH_SHORT).show();
  }

  public String getDescription() {
    return mDescriptionEditText.getText().toString();
  }

  public void setDescription(String description) {
    if (description != null && !TextUtils.isOnlyWhiteSpaces(description)) {
      mDescriptionEditText.setText(description);
    }
  }

  public void hideKeyboard() {
    InputMethodManager imm =
        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_send_quadrilateral;
  }

  @Override
  protected SendQuadrilateralActionViewModel getSpecificViewModel() {
    return mViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.menu_action_send_quadrilateral_title);
  }

  private void setupFocusOrder() {
    setupFocusOrder(mPicker1, mPicker2);
    setupFocusOrder(mPicker2, mPicker3);
    setupFocusOrder(mPicker3, mPicker4);
  }

  private void setupFocusOrder(LatLongPicker srcPicker, LatLongPicker dstPicker) {
    srcPicker.setOnFocusLostListener(dstPicker::requestFocus);
  }
}
