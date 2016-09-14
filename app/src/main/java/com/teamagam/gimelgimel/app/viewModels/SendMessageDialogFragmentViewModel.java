
package com.teamagam.gimelgimel.app.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.presentation.interfaces.PresenterSharedPreferences;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import rx.Subscription;


public class SendMessageDialogFragmentViewModel implements ViewModel {

    private static final String TAG = "SendMessageDialogFragmentViewModel";
    private Activity activity;
    protected Logger sLogger = LoggerFactory.create(this.getClass());
    private SendMessageInteractor mSendMessageInteractor;

    @BindView(R.id.dialog_send_text_message_edit_text)
    EditText mSendMessageEditText;

    @BindString(R.string.dialog_send_message_invalid_empty_message)
    String mInvalidMessage;

    @Inject
    SendMessagePresenter sendMessagePresenter;

    private Subscription subscription;
    private SendMessagePresenter.View mView;

    private boolean isInputValid(String userMessage) {
        return !userMessage.isEmpty();
    }

    private void showError() {
        mSendMessageEditText.setError(mInvalidMessage);
        mSendMessageEditText.requestFocus();
    }

    @Inject
    public SendMessageDialogFragmentViewModel(Activity activity, SendMessagePresenter.View view) {
        this.activity = activity;
        this.mView = view;

        //todo: add butterknife bind
    }

    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        this.activity = null;
    }


    public boolean sendTextMessage() {
        String userMessage = mSendMessageEditText.getText().toString();

        if (!isInputValid(userMessage)) {
            showError();
            sLogger.userInteraction("Clicked OK - invalid input");
            return false;
        } else {
            sLogger.userInteraction("Clicked OK");
            //todo: no need to set this view, its is dismissed right away
            sendMessagePresenter.setView(mView);

            //todo: fix preferences
            String userName = this.activity.getBaseContext().getString(R.string.user_name_text_key);

            MessageText msg = new MessageText(userName, userMessage);
           // mSendMessageInteractor.sendMessage(msg, sendMessagePresenter.createSubscriber());

           // mView.dismiss();
            return true;
        }
    }

    private class SendMessageSubscriber extends BaseSubscriber<Message> {
        @Override
        public void onError(Throwable e) {
            mView.showError(e.getMessage());
        }

        @Override
        public void onNext(Message message) {
            mView.displayMessageStatus();
        }
    }
}



