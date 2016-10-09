package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import rx.Observable;
import rx.Subscriber;

/**
 * {@link SyncInteractor} with generic type {@link GeoEntityNotification} to sync the displayed map with it's
 * repository. all of the entities that appear on the map should be in the
 * {@link DisplayedEntitiesRepository}.
 */
@AutoFactory
public class GetMessagesInteractor extends SyncInteractor<Message> {

    private final MessagesRepository mMessagesRepository;

    GetMessagesInteractor(@Provided ThreadExecutor threadExecutor,
                          @Provided PostExecutionThread postExecutionThread,
                          @Provided MessagesRepository messagesRepository,
                          Subscriber<Message> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mMessagesRepository = messagesRepository;
    }

    @Override
    protected Observable<Message> buildUseCaseObservable() {
        return mMessagesRepository.getMessages();
    }
}
