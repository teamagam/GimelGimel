package com.teamagam.gimelgimel.app.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.LatLongPicker;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import javax.inject.Inject;

public class GoToLocationDialogFragment extends DialogFragment {

  @BindView(R.id.dialog_go_to_lat_long)
  LatLongPicker mLatLongPicker;
  @Inject
  GoToLocationViewModelFactory mGoToLocationViewModelFactory;
  private GoToLocationViewModel mViewModel;
  private AlertDialog mAlertDialog;
  private View mInnerView;

  public static GoToLocationDialogFragment newInstance() {
    return new GoToLocationDialogFragment();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_go_to_title)
        .setView(createView())
        .setPositiveButton(R.string.dialog_go_to_positive_button,
            (dialog, which) -> mViewModel.onPositiveButtonClicked())
        .create();
    return mAlertDialog;
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, mInnerView);
    mViewModel = mGoToLocationViewModelFactory.create(mLatLongPicker, this);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    mViewModel.start();
  }

  @Override
  public void onPause() {
    super.onPause();
    mViewModel.stop();
  }

  public void setPositiveButtonEnabled(boolean enabled) {
    getPositiveButton().setEnabled(enabled);
  }

  private Button getPositiveButton() {
    return mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
  }

  @SuppressLint("InflateParams")
  private View createView() {
    mInnerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_go_to, null);
    return mInnerView;
  }
}
