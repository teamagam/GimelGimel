package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class SendMessageDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_send_message_edit_text)
    EditText mSendMessageEditText;

    private GGMessageSender mMessageSender;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMessageSender = new GGMessageSender(activity);
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        ButterKnife.bind(this, dialogView);
    }

    @Override
    protected void onPositiveClick() {
        String userMessage = mSendMessageEditText.getText().toString();

        if (!isInputValid(userMessage)) {
            showError();
            sLogger.userInteraction("Clicked OK - invalid input");
        } else {
            sLogger.userInteraction("Clicked OK");
            mMessageSender.sendTextMessageAsync(userMessage);
            dismiss();
        }

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
    protected int getFragmentLayout() {
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

    private void showError() {
        mSendMessageEditText.setError(
                getString(R.string.dialog_send_message_invalid_empty_message));
        mSendMessageEditText.requestFocus();
    }

    private boolean isInputValid(String userMessage) {
        return !userMessage.isEmpty();
    }
}
