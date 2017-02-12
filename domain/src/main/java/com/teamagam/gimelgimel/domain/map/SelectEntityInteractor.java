package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

@AutoFactory
public class SelectEntityInteractor extends DoInteractor {

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
    protected Observable buildUseCaseObservable() {
        return Observable.just(mEntityId)
                .flatMap(mEntityMessageMapper::getMessageId)
                .flatMap(mMessagesRepository::getMessage)
                .filter(m -> m != null)
                .doOnNext(mMessagesRepository::selectMessage);
    }
}
