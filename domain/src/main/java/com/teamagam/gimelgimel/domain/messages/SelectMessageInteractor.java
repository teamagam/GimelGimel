package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

/**
 * {@link DoInteractor} with String as generic type for selecting message.
 */
@AutoFactory
public class SelectMessageInteractor extends DoInteractor<String> {

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
    protected Observable<String> buildUseCaseObservable() {
        return rx.Observable.just(mMessageId)
                .doOnNext(mMessagesRepository::selectMessage);
    }

}
