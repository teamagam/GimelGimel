package com.teamagam.gimelgimel.app.message.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import butterknife.BindView;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.ViewDismisser;
import com.teamagam.gimelgimel.app.common.base.view.fragments.dialogs.BaseBindingDialogFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.message.model.PointGeometryParcel;
import com.teamagam.gimelgimel.app.message.viewModel.SendGeoMessageViewModel;
import com.teamagam.gimelgimel.databinding.DialogSendGeoMessageBinding;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import javax.inject.Inject;

/**
 * Sending geographical message dialog.
 * Displays coordinates to be sent with an OK/Cancel buttons.
 * On OK will send geographical message to GGMessaging server and place a pin at
 * associated geographical location.
 */
public class SendGeographicMessageDialog extends BaseBindingDialogFragment
    implements ViewDismisser {

  private static final String ARG_POINT_GEOMETRY =
      SendGeographicMessageDialog.class.getSimpleName() + "_PointGeometry";

  @Inject
  SendGeoMessageViewModel mViewModel;

  @BindView(R.id.spinner_types)
  Spinner mSpinner;

  public static SendGeographicMessageDialog newInstance(PointGeometry pointGeometry) {
    SendGeographicMessageDialog fragment = new SendGeographicMessageDialog();

    Bundle args = new Bundle();
    args.putParcelable(ARG_POINT_GEOMETRY, PointGeometryParcel.create(pointGeometry));
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    refreshPositiveButtonEnabledState();
    setSpinnerListener();
  }

  private void setSpinnerListener() {
    mSpinner.setOnItemClickListener();
  }

  @Override
  protected int getTitleResId() {
    return R.string.dialog_message_geo_title;
  }

  @Override
  protected int getDialogLayout() {
    return R.layout.dialog_send_geo_message;
  }

  @Override
  protected String getNegativeString() {
    return getString(android.R.string.cancel);
  }

  @Override
  protected String getPositiveString() {
    return getString(R.string.dialog_message_geo_send);
  }

  @Override
  protected View onCreateDialogLayout() {
    DialogSendGeoMessageBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(getActivity()), getDialogLayout(), null, false);
    PointGeometry point =
        getArguments().<PointGeometryParcel>getParcelable(ARG_POINT_GEOMETRY).convert();
    mViewModel.init(this, point);
    binding.setViewModel(mViewModel);
    bindPositiveButtonEnabledState();
    return binding.getRoot();
  }

  @Override
  protected boolean hasNegativeButton() {
    return true;
  }

  @Override
  protected boolean hasPositiveButton() {
    return true;
  }

  @Override
  protected void onPositiveClick() {
    mViewModel.onPositiveClick();
  }

  private void bindPositiveButtonEnabledState() {
    mViewModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override
      public void onPropertyChanged(Observable observable, int i) {
        if (i == BR.inputNotValid) {
          refreshPositiveButtonEnabledState();
        }
      }
    });
  }

  private void refreshPositiveButtonEnabledState() {
    mDialog.getButton(DialogInterface.BUTTON_POSITIVE).
        setEnabled(!mViewModel.isInputNotValid());
  }

  private class OnItemSelected implements Spinner.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      mViewModel.
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
  }
}
