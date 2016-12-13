package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscription;

public class DataSubscriptionRequest<T> implements BaseInteractor.SubscriptionRequest {

    protected Observable<T> mObservable;
    protected ThreadExecutor mThreadExecutor;

    protected DataSubscriptionRequest(ThreadExecutor threadExecutor, Observable<T> observable) {
        mThreadExecutor = threadExecutor;
        mObservable = observable;
    }


    public Subscription subscribe() {
        return mObservable
                .subscribeOn(mThreadExecutor.getScheduler())
                .subscribe();
    }

    public static class SubscriptionRequestFactory {
        private final ThreadExecutor mThreadExecutor;

        public SubscriptionRequestFactory(
                ThreadExecutor threadExecutor) {
            mThreadExecutor = threadExecutor;
        }

        public BaseInteractor.SubscriptionRequest create(Observable observable) {
            return new DataSubscriptionRequest(mThreadExecutor, observable);
        }
    }
}
