package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * the setlected message should be emitted on every subscription
  */
@AutoFactory
public class SyncSelectedMessageInteractor extends SyncInteractor<Message> {

    private final MessagesRepository mMessagesRepository;

    protected SyncSelectedMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided  PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            Subscriber<Message> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mMessagesRepository = messagesRepository;
    }

    @Override
    protected Observable<Message> buildUseCaseObservable() {
        return mMessagesRepository.getSyncSelectedMessageObservable();

    }
}
