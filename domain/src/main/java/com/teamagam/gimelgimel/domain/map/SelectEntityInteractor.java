package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class SelectEntityInteractor extends BaseDataInteractor {

    private EntityMessageMapper mEntityMessageMapper;
    private final MessagesRepository mMessagesRepository;
    private String mEntityId;

    protected SelectEntityInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided EntityMessageMapper entityMessageMapper,
            @Provided MessagesRepository messagesRepository,
            String entityId) {
        super(threadExecutor);
        mEntityMessageMapper = entityMessageMapper;
        mMessagesRepository = messagesRepository;
        mEntityId = entityId;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest selectRelatedMessage =
                factory.create(createSelectRelatedMessageObservable());

        return Collections.singletonList(selectRelatedMessage);
    }

    private Observable<Message> createSelectRelatedMessageObservable() {
        return Observable.just(mEntityId)
                .flatMap(mEntityMessageMapper::getMessageId)
                .flatMap(mMessagesRepository::getMessage)
                .filter(m -> m != null)
                .doOnNext(mMessagesRepository::selectMessage);
    }
}
