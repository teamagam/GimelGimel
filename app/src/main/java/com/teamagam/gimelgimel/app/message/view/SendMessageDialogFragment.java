package com.teamagam.gimelgimel.app.message.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.view.LayoutInflater;
import android.view.View;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.SendSimpleMessageViewModel;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.common.base.view.fragments.dialogs.BaseBindingDialogFragment;
import com.teamagam.gimelgimel.databinding.DialogSendMessageBinding;

import javax.inject.Inject;

/**
 * Fragment that send a textual message
 */
public class SendMessageDialogFragment extends BaseBindingDialogFragment
        implements SendSimpleMessageViewModel.IViewDismisser {

    @Inject
    SendSimpleMessageViewModel mViewModel;

    @Override
    protected boolean hasPositiveButton() {
        return true;
    }


    @Override
    protected boolean hasNegativeButton() {
        return true;
    }

    @Override
    protected String getPositiveString() {
        return getString(R.string.dialog_send_message_ok);
    }

    @Override
    protected String getNegativeString() {
        return getString(R.string.dialog_send_message_cancel);
    }

    @Override
    protected int getTitleResId() {
        return R.string.dialog_send_message_title;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_send_message;
    }

    @Override
    protected View onCreateDialogLayout() {
        DialogSendMessageBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getActivity()), getDialogLayout(),
                null, false);
        mViewModel.setView(this);
        binding.setViewModel(mViewModel);
        bindOkButtonEnabledState();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPositiveButtonEnabledState();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected void onPositiveClick() {
        mViewModel.onPositiveClick();
    }


    private void bindOkButtonEnabledState() {
        mViewModel.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
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
