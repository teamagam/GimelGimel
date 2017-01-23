package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscription;

public class DataSubscriptionRequest<T> implements BaseInteractor.SubscriptionRequest {

    private final Observable<T> mObservable;
    private final ThreadExecutor mThreadExecutor;

    private DataSubscriptionRequest(ThreadExecutor threadExecutor, Observable<T> observable) {
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

        SubscriptionRequestFactory(
                ThreadExecutor threadExecutor) {
            mThreadExecutor = threadExecutor;
        }

        public <T> DataSubscriptionRequest create(Observable<T> observable) {
            return new DataSubscriptionRequest<>(mThreadExecutor, observable);
        }
    }
}
