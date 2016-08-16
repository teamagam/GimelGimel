package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;

@PerFragment
public class SendMessagePresenter extends AbstractPresenter {

    View mView;

    private SendMessageInteractor mSendMessageInteractor;

    @Inject
    public SendMessagePresenter(SendMessageInteractor sendMessageInteractor) {
        super();
        mSendMessageInteractor = sendMessageInteractor;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    public void resume() {
        mView.showProgress();
    }

    @Override
    public void pause() {
        mView.hideProgress();
    }

    @Override
    public void stop() {
        mView.hideProgress();
    }

    @Override
    public void destroy() {
        mView.hideProgress();
    }


    public void sendMessage(String userMessage) {
        MessageText msg = new MessageText("Sender", userMessage);
        mSendMessageInteractor.sendMessage(msg, new SendMessageSubscriber());
    }

    public interface View extends BaseView {
        void displayMessageStatus();
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
