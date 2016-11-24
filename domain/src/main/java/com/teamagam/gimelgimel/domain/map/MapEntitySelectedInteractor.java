package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

@AutoFactory
public class MapEntitySelectedInteractor extends DoInteractor<Message> {

    private final MessagesRepository mMessageRepository;
    private final String mEntityId;

    protected MapEntitySelectedInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            String entityId) {
        super(threadExecutor);
        mMessageRepository = messagesRepository;
        mEntityId = entityId;
    }


    @Override
    protected Observable<Message> buildUseCaseObservable() {
        return Observable.just(mEntityId)
                .flatMap(mMessageRepository::getMessage)
                .filter(o -> o != null)
                .doOnNext(mMessageRepository::selectMessage);
    }
}
