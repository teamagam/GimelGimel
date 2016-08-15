package com.teamagam.gimelgimel.presentation.presenters.impl;

import com.gimelgimel.domain.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;

import java.util.concurrent.Executor;

import presenters.SendMessageDialogPresenter;
import presenters.base.AbstractPresenter;
import presenters.base.MainThread;

public class SendMessageDialogPresenterImpl extends AbstractPresenter implements SendMessageDialogPresenter {

    View mView;
    MessagesRepository mMessagesRepository;

    private SendMessageInteractor sendMessageInteractor;

    public SendMessageDialogPresenterImpl(Executor executor, MainThread mainThread, View view,
                                          SendMessageInteractor sendMessageInteractor,
                                          MessagesRepository messagesRepository) {
        super(executor, mainThread);

        mView = view;
        mMessagesRepository = messagesRepository;
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


}
