package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * Created on 10/10/2016.
 */
@AutoFactory
public class SyncNumReadMessagesInteractor extends SyncInteractor<Integer> {

    private final MessagesRepository mMessagesRepository;

    protected SyncNumReadMessagesInteractor (
            @Provided ThreadExecutor threadExecutor,
            @Provided  PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            Subscriber<Integer> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mMessagesRepository = messagesRepository;
    }

    @Override
    protected Observable<Integer> buildUseCaseObservable() {
        return mMessagesRepository.getSyncNumReadObservable();

    }
}
