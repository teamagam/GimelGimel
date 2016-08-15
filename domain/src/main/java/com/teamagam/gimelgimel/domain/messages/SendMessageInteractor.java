package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.messages.entities.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

public class SendMessageInteractor extends AbstractInteractor {


    private final MessagesRepository messagesRepository;
    private Message message = null;

    public SendMessageInteractor(MessagesRepository messagesRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.messagesRepository = messagesRepository;
    }

    public void init(Message message){
        this.message = message;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if(message == null){
            throw new IllegalArgumentException("init(message) not called, or called with null " +
                    "argument.");
        }
        return this.messagesRepository.sendMessage(this.message)
                .doOnNext(this.messagesRepository::putMessage);
    }
}
