package com.teamagam.gimelgimel.domain.base.interactors;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

abstract class AbsInteractor<T> implements Interactor {

    private Subscription subscription;

    public final void execute() {
        this.subscription = buildObservable()
                .subscribe(getSubscriber());
    }

    public final void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link SyncInteractor}.
     */
    protected abstract Observable<T> buildObservable();

    protected abstract Subscriber<T> getSubscriber();
}
