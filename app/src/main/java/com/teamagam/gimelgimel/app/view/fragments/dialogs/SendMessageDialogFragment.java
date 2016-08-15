package com.teamagam.gimelgimel.app.view.fragments.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.injectors.components.DaggerMessagesComponent;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.base.BaseDialogFragment;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;
import com.teamagam.gimelgimel.presentation.presenters.impl.SendMessagePresenterImpl;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Fragment that send
 * todo: complete
 */
public class SendMessageDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_send_geo_message_edit_text)
    EditText mSendMessageEditText;

    //    private GGMessageSender mMessageSender;
    @Inject
    SendMessagePresenterImpl sendMessagePresenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        mMessageSender = ((GGApplication) getActivity().getApplicationContext()).getMessageSender();
        DaggerMessagesComponent.builder()
                .applicationComponent(((GGApplication) getActivity().getApplication()).getApplicationComponent())
                .messageModule(new MessageModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onCreateDialogLayout(View dialogView) {
    }

    @Override
    protected void onPositiveClick() {
        String userMessage = mSendMessageEditText.getText().toString();

        if (!isInputValid(userMessage)) {
            showError();
            sLogger.userInteraction("Clicked OK - invalid input");
        } else {
            sLogger.userInteraction("Clicked OK");
            final Context context = getActivity().getApplicationContext();
            sendMessagePresenter.sendMessage(userMessage, new SendMessagePresenter.View(){
                @Override
                public void showProgress() {

                }

                @Override
                public void hideProgress() {

                }

                @Override
                public void showError(String s) {

                }

                @Override
                public void displayMessageStatus() {
                    Toast.makeText(context, "completed", Toast.LENGTH_LONG).show();
                }
            });
//            mMessageSender.sendTextMessageAsync(userMessage);
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

    private void showError() {
        mSendMessageEditText.setError(
                getString(R.string.dialog_send_message_invalid_empty_message));
        mSendMessageEditText.requestFocus();
    }

    private boolean isInputValid(String userMessage) {
        return !userMessage.isEmpty();
    }
}
