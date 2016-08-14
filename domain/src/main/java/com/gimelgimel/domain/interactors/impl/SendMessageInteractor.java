package com.gimelgimel.domain.interactors.impl;

import com.gimelgimel.domain.executor.PostExecutionThread;
import com.gimelgimel.domain.executor.ThreadExecutor;
import com.gimelgimel.domain.interactors.base.AbstractInteractor;
import com.gimelgimel.domain.model.MessageModel;
import com.gimelgimel.domain.repository.MessagesRepository;

import rx.Observable;

public class SendMessageInteractor extends AbstractInteractor {

    private final MessagesRepository mMessagesRepository;
    private MessageModel mMessage;

    public SendMessageInteractor(MessagesRepository messagesRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                 MessageModel message) {
        super(threadExecutor, postExecutionThread);

        mMessagesRepository = messagesRepository;
        mMessage = message;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if(mMessage == null){
            throw new IllegalArgumentException("init(message) not called, or called with null " +
                    "argument.");
        }
        return this.mMessagesRepository.sendMessage(this.mMessage)
                .doOnNext(this.mMessagesRepository::putMessage);
    }
}
