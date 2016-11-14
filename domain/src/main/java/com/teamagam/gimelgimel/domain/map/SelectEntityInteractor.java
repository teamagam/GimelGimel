package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

/**
 * Created on 11/9/2016.
 * TODO: complete text
 */
@AutoFactory
public class SelectEntityInteractor extends DoInteractor{

    private final MessagesRepository mMessagesRepostory;
    private String mLayerId;
    private String mEntityId;

    protected SelectEntityInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            String layerId,
            String entityId) {
        super(threadExecutor);
        mMessagesRepostory = messagesRepository;
        mLayerId = layerId;
        mEntityId = entityId;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.just(mEntityId)
                .flatMap(mMessagesRepostory::getMessageById)
                .doOnNext(mMessagesRepostory::selectMessage);
    }
}
