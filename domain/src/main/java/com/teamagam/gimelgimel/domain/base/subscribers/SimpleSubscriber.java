package com.teamagam.gimelgimel.domain.base.subscribers;

import com.teamagam.gimelgimel.domain.base.logging.DomainLogger;
import com.teamagam.gimelgimel.domain.base.logging.DomainLoggerFactoryHolder;

import rx.Subscriber;

/**
 * Simple subscriber that does nothing on any event
 */

public class SimpleSubscriber<T> extends Subscriber<T> {

    private static final DomainLogger sLogger = DomainLoggerFactoryHolder.get().create(
            SimpleSubscriber.class.getSimpleName());

    @Override
    public void onCompleted() {
        //do nothing
    }

    @Override
    public void onError(Throwable e) {
        sLogger.e("Observable onError", e);
        //do nothing
    }

    @Override
    public void onNext(T t) {
        //do nothing
    }
}
