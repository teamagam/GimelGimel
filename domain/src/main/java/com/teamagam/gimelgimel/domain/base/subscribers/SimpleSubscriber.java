package com.teamagam.gimelgimel.domain.base.subscribers;

import rx.Subscriber;

/**
 * Simple subscriber that does nothing on any event
 */

public class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
        //do nothing
    }

    @Override
    public void onError(Throwable e) {
        //do nothing
    }

    @Override
    public void onNext(T t) {
        //do nothing
    }
}
