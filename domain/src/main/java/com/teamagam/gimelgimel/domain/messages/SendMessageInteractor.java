package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

public class SendMessageInteractor extends AbstractInteractor {


    private final MessagesRepository messagesRepository;
    private Message mMessage = null;

    public SendMessageInteractor(MessagesRepository messagesRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.messagesRepository = messagesRepository;
    }

    public void sendMessage(Message message, Subscriber subscriber){
        mMessage = message;
        execute(subscriber);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if(mMessage == null){
            throw new IllegalArgumentException("sendMessage(mMessage) not called, or called with null " +
                    "argument.");
        }
        return this.messagesRepository.sendMessage(mMessage)
                .doOnNext(this.messagesRepository::putMessage);
    }
}
