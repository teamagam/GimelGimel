package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.services.GGMessagingUtils;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class SendMessageDialogFragment extends BaseDialogFragment {

    private EditText mSendMessageEditText;

    @Override
    protected void onCreateDialogLayout(View dialogView) {
        mSendMessageEditText = (EditText) dialogView.findViewById(
                R.id.dialog_send_message_edit_text);
    }

    @Override
    protected void onPositiveClick() {
        String userMessage = mSendMessageEditText.getText().toString();
        if (userMessage.isEmpty()) {
            mSendMessageEditText.setError(
                    getString(R.string.dialog_send_message_invalid_empty_message));
            mSendMessageEditText.requestFocus();
            return;
        }

        GGMessagingUtils.sendTextMessageAsync(userMessage);

        dismiss();
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
}
