package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

import rx.Observable;
import rx.Subscriber;

/**
 * Base interactor for acting on model to change data
 * @deprecated
 */
public abstract class DoInteractor<T> extends AbsInteractor<T> {

    private final ThreadExecutor threadExecutor;

    protected DoInteractor(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }

    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link SyncInteractor}.
     */
    protected Observable<T> buildObservable() {
        return buildUseCaseObservable()
                .subscribeOn(threadExecutor.getScheduler());
    }

    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link DoInteractor}.
     */
    protected abstract Observable<T> buildUseCaseObservable();

    /**
     * Creates subscriber that handles interactor's observable observed items.
     * Override to change behaviour.
     */
    protected Subscriber<T> getSubscriber() {
        return new SimpleSubscriber<>();
    }
}
