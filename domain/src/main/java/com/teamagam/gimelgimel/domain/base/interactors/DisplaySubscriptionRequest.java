package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class DisplaySubscriptionRequest<T> implements BaseInteractor.SubscriptionRequest {

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final Observable<T> mObservable;
    private final Subscriber<T> mSubscriber;

    private DisplaySubscriptionRequest(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       Observable<T> observable,
                                       Subscriber<T> subscriber) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mObservable = observable;
        mSubscriber = subscriber;
    }

    @Override
    public Subscription subscribe() {
        return mObservable
                .subscribeOn(mThreadExecutor.getScheduler())
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(mSubscriber);
    }

    public static class DisplaySubscriptionRequestFactory {
        private final ThreadExecutor mThreadExecutor;
        private final PostExecutionThread mPostExecutionThread;

        DisplaySubscriptionRequestFactory(
                ThreadExecutor threadExecutor,
                PostExecutionThread postExecutionThread) {
            mThreadExecutor = threadExecutor;
            mPostExecutionThread = postExecutionThread;
        }

        public <T> DisplaySubscriptionRequest create(Observable<T> observable,
                                                     Subscriber<T> subscriber) {
            return new DisplaySubscriptionRequest<>(mThreadExecutor, mPostExecutionThread,
                    observable, subscriber);
        }

        public <T> DisplaySubscriptionRequest create(Observable<T> observable,
                                                     Action1<T> subscriberOnNext) {
            Subscriber<T> subscriber = new SimpleSubscriber<T>() {
                @Override
                public void onNext(T o) {
                    subscriberOnNext.call(o);
                }
            };
            return new DisplaySubscriptionRequest<>(mThreadExecutor, mPostExecutionThread,
                    observable, subscriber);
        }
    }
}
