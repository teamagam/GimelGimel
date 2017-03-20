package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class SelectMessageInteractor extends BaseDataInteractor {

    private final MessagesRepository mMessagesRepository;
    private final String mMessageId;

    protected SelectMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            String messageId) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
        mMessageId = messageId;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return Collections.singletonList(buildSelectMessageRequest(factory));
    }

    private DataSubscriptionRequest buildSelectMessageRequest(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return factory.create(
                Observable.just(mMessageId),
                entityIdObservable ->
                        entityIdObservable
                                .flatMap(mMessagesRepository::getMessage)
                                .filter(m -> m != null)
                                .doOnNext(mMessagesRepository::selectMessage)
        );
    }
}