package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

/**
 * {@link DoInteractor} with String as generic type for selecting message.
 */
@AutoFactory
public class SelectMessageInteractor extends DoInteractor<Message> {

    private final MessagesRepository mMessagesRepository;

    private String mMessageId;

    protected SelectMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            String messageId) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
        mMessageId = messageId;
    }


    @Override
    protected Observable<Message> buildUseCaseObservable() {
        return Observable.just(mMessageId)
                .flatMap(mMessagesRepository::getMessage)
                .doOnNext(mMessagesRepository::selectMessage)
                .doOnNext(mMessagesRepository::markMessageRead);
    }
}
