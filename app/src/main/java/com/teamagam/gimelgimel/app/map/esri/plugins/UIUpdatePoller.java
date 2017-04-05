package com.teamagam.gimelgimel.app.map.esri.plugins;


import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;

abstract class UIUpdatePoller {

    private final Scheduler mWorkScheduler;
    private Subscription mSubscription;
    private boolean mIsRunning;

    public UIUpdatePoller(Scheduler workScheduler) {
        mWorkScheduler = workScheduler;
        mSubscription = null;
        mIsRunning = false;
    }

    public void start() {
        if (!mIsRunning) {
            mSubscription =
                    Observable.interval(Constants.UI_REFRESH_RATE_MS, TimeUnit.MILLISECONDS)
                            .observeOn(mWorkScheduler)
                            .doOnNext(x -> periodicalAction())
                            .subscribe(new SimpleSubscriber<>());
            mIsRunning = true;
        }
    }

    public void stop() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mIsRunning = false;
    }

    protected abstract void periodicalAction();
}