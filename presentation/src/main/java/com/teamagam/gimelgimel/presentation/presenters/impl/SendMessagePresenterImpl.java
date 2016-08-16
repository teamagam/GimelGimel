package com.teamagam.gimelgimel.presentation.presenters.impl;

import com.teamagam.gimelgimel.presentation.scopes.PerFragment;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;

import javax.inject.Inject;

import rx.Subscriber;

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
        mSendMessageInteractor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                completed.displayMessageStatus();
            }

            @Override
            public void onError(Throwable e) {
                completed.showError(e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                completed.displayMessageStatus();
            }
        });
    }
}
