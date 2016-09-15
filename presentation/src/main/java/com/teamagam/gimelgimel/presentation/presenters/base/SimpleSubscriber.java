package com.teamagam.gimelgimel.presentation.presenters.base;

import rx.Subscriber;

/**
 * Simple rx subscriber with must implement onNext method
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T>{

    @Override
    public void onCompleted() {
        //no-op
    }

    @Override
    public void onError(Throwable e) {
        //no-op
    }

}
