package com.teamagam.gimelgimel.domain.base.interactors;

import rx.Subscriber;

/**
 * Base {@link Subscriber} with generic parameter T that does nothing.
 * used for {@link DoInteractor} and may be override when necessary.
 */
class DoNothingSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        // no-op by default.
    }

    @Override
    public void onError(Throwable e) {
        // no-op by default.
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }
}
