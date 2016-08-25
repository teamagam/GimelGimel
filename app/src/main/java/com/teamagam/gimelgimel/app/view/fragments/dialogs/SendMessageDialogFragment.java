package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.injectors.components.DaggerMessagesComponent;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.app.viewModels.SendMessageDialogFragmentViewModel;
import com.teamagam.gimelgimel.databinding.DialogSendMessageBinding;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;
import javax.inject.Inject;
import butterknife.BindView;

/**
 * Fragment that send a textual message
 */
public class SendMessageDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_send_text_message_edit_text)
    EditText mSendMessageEditText;

    @Inject
    SendMessagePresenter sendMessagePresenter;

    private DialogSendMessageBinding mBinding;
    private SendMessageDialogFragmentViewModel mSendMessageViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_send_message, null, false);
        mSendMessageViewModel = new SendMessageDialogFragmentViewModel(getActivity());
        mBinding.setViewModel(mSendMessageViewModel);
    }

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
    protected Object castInterface(Activity activity) {
        return activity;
    }

    @Override
    protected Object castInterface(Fragment fragment) {
        return fragment;
    }

    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DaggerMessagesComponent.builder()
                .applicationComponent(((GGApplication) getActivity().getApplication()).getApplicationComponent())
                .messageModule(new MessageModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onPositiveClick() {
        super.onPositiveClick();
        String userMessage = mSendMessageEditText.getText().toString();


    }
}
