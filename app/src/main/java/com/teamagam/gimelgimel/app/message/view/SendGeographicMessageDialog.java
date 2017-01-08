package com.teamagam.gimelgimel.app.message.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.viewModel.SendGeoMessageViewModel;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.common.base.view.fragments.dialogs.BaseBindingDialogFragment;
import com.teamagam.gimelgimel.databinding.DialogSendGeoMessageBinding;

import javax.inject.Inject;

/**
 * Sending geographical message dialog.
 * Displays coordinates to be sent with an OK/Cancel buttons.
 * On OK will send geographical message to GGMessaging server and place a pin at
 * associated geographical location.
 */
public class SendGeographicMessageDialog extends BaseBindingDialogFragment implements SendGeoMessageViewModel.IViewDismisser {

    private static final String ARG_POINT_GEOMETRY = SendGeographicMessageDialog.class
            .getSimpleName() + "_PointGeometry";

    @Inject
    SendGeoMessageViewModel mViewModel;

    /**
     * Works the same as {@link SendGeographicMessageDialog#newInstance(PointGeometryApp pointGeometry,
     * Fragment targetFragment)) method
     * without settings a target fragment
     */
    public static SendGeographicMessageDialog newInstance(PointGeometryApp pointGeometry) {
        SendGeographicMessageDialog fragment = new SendGeographicMessageDialog();

        Bundle args = new Bundle();
        args.putParcelable(ARG_POINT_GEOMETRY, pointGeometry);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Utility for creation of {@link SendGeographicMessageDialog} with a {@link PointGeometryApp}
     * bundled argument (split into primitives) and containing {@link Fragment} (target-fragment)
     * to be used if necessary
     *
     * @param pointGeometry  - the point to pass as argument (as primitives)
     * @param targetFragment - the containing fragment
     * @return bundled fragment with the given {@link PointGeometryApp}
     */
    public static SendGeographicMessageDialog newInstance(PointGeometryApp pointGeometry,
                                                          Fragment targetFragment) {
        SendGeographicMessageDialog f = newInstance(pointGeometry);
        f.setTargetFragment(targetFragment, 0 /*optional*/);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected int getTitleResId() {
        return R.string.dialog_send_message_geo_title;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_send_geo_message;
    }

    @Override
    protected String getNegativeString() {
        return getString(R.string.dialog_send_message_cancel);
    }

    @Override
    protected String getPositiveString() {
        return getString(R.string.dialog_send_message_ok);
    }

    @Override
    protected View onCreateDialogLayout() {
        DialogSendGeoMessageBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getActivity()), getDialogLayout(),
                null, false);
        PointGeometryApp point = getArguments().getParcelable(ARG_POINT_GEOMETRY);
        mViewModel.init(this, point);
        binding.setViewModel(mViewModel);
        bindPositiveButtonEnabledState();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPositiveButtonEnabledState();
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
}
