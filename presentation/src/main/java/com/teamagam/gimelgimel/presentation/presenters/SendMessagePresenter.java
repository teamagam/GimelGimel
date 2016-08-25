package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.presentation.interfaces.PresenterSharedPreferences;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import javax.inject.Inject;

import rx.Subscriber;

@PerActivity
public class SendMessagePresenter extends AbstractPresenter<SendMessagePresenter.View, Message> {

    private final PresenterSharedPreferences mSharedPreferences;
    private View mView;

    private SendMessageInteractor mSendMessageInteractor;

    @Inject
    public SendMessagePresenter(SendMessageInteractor sendMessageInteractor, PresenterSharedPreferences
            sharedPreferences) {
        super();
        mSendMessageInteractor = sendMessageInteractor;
        mSharedPreferences = sharedPreferences;
    }

    public void setView(View view) {
        mView = view;
    }


    public void sendMessage(String userMessage) {
        MessageText msg = new MessageText(mSharedPreferences.getSenderName(), userMessage);
        mSendMessageInteractor.sendMessage(msg, new SendMessageSubscriber());
    }

    public void onNext(Message message) {

    }

    @Override
    public Subscriber<Message> createSubscriber() {
        return null;
    }

    public interface View extends BaseView {
        void displayMessageStatus();

        void showProgress();
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
