package com.teamagam.gimelgimel.presentation.presenters.impl;

import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;

@PerFragment
public class SendMessagePresenterImpl extends AbstractPresenter implements SendMessagePresenter {

    View mView;
    MessagesRepository mMessagesRepository;

    private SendMessageInteractor mSendMessageInteractor;

    @Inject
    public SendMessagePresenterImpl(SendMessageInteractor sendMessageInteractor) {
        super();
        mSendMessageInteractor = sendMessageInteractor;
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

    @Override
    public void onError(String message) {
        mView.hideProgress();
        mView.showError(message);
    }


    @Override
    public void sendMessage(String userMessage, final View completed) {
        MessageText msg = new MessageText("Sender", userMessage);
        mSendMessageInteractor.init(msg);
        mSendMessageInteractor.execute(new BaseSubscriber<Message>() {
            @Override
            public void onError(Throwable e) {
                completed.showError(e.getMessage());
            }

            @Override
            public void onNext(Message message) {
                completed.displayMessageStatus();
            }
        });

    }
}
