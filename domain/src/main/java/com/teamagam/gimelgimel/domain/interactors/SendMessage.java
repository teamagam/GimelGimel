package com.teamagam.gimelgimel.domain.interactors;

import com.teamagam.gimelgimel.domain.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entities.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

public class SendMessage extends BaseUseCase {


    private final MessagesRepository messagesRepository;
    private Message message = null;

    public SendMessage(MessagesRepository messagesRepository,
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
