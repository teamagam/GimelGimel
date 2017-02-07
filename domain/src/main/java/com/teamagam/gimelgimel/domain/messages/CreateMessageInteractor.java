package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * Base interactor for use cases that creates messages.
 * Executes the message creation on the subscription thread.
 */
public abstract class CreateMessageInteractor<T extends Message> extends DoInteractor<T> {
    private final UserPreferencesRepository mUserPreferencesRepository;

    private T mMessage;

    protected CreateMessageInteractor(
            ThreadExecutor threadExecutor,
            UserPreferencesRepository userPreferences) {
        super(threadExecutor);
        mUserPreferencesRepository = userPreferences;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return Observable.create((Subscriber<? super T> subscriber) -> {
            try {
                String senderId = getSenderId();
                mMessage = createMessage(senderId);
                subscriber.onNext(mMessage);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    protected final String getSenderId() {
        return mUserPreferencesRepository.getString(Constants.USERNAME_PREFRENCE_KEY);
    }

    protected abstract T createMessage(String senderId);

    protected T getMessage() {
        return mMessage;
    }
}
