package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * Base interactor for use cases that creates messages.
 * Executes the message creation on the subscription thread.
 */
public abstract class CreateMessageInteractor<T extends Message> extends AbstractInteractor<T> {
    private final UserPreferencesRepository mUserPreferencesRepository;

    protected CreateMessageInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            UserPreferencesRepository userPreferences) {
        super(threadExecutor, postExecutionThread);
        mUserPreferencesRepository = userPreferences;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create((Subscriber<? super String> subscriber) -> {
            subscriber.onNext(mUserPreferencesRepository.getSenderId());
            subscriber.onCompleted();
        }).map(this::createMessage);
    }

    protected abstract T createMessage(String senderId);
}
