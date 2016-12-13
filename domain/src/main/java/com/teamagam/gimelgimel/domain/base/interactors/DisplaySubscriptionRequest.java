package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class DisplaySubscriptionRequest<T> extends DataSubscriptionRequest<T> {

    private final Subscriber<T> mSubscriber;
    private final PostExecutionThread mPostExecutionThread;


    protected DisplaySubscriptionRequest(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         Observable<T> observer,
                                         Subscriber<T> subscriber) {
        super(threadExecutor, observer);
        mPostExecutionThread = postExecutionThread;
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

        public DisplaySubscriptionRequestFactory(
                ThreadExecutor threadExecutor,
                PostExecutionThread postExecutionThread) {
            mThreadExecutor = threadExecutor;
            mPostExecutionThread = postExecutionThread;
        }

        public <T> DisplaySubscriptionRequest create(Observable<T> observable,
                                                 Subscriber<T> subscriber) {
            return new DisplaySubscriptionRequest(mThreadExecutor, mPostExecutionThread, observable,
                    subscriber);
        }

        public <T> DisplaySubscriptionRequest create(Observable<T> observable,
                                                 Action1<T> subscriberOnNext) {
            Subscriber<T> subscriber = new SimpleSubscriber<T>() {
                @Override
                public void onNext(T o) {
                    subscriberOnNext.call(o);
                }
            };
            return new DisplaySubscriptionRequest(mThreadExecutor, mPostExecutionThread, observable,
                    subscriber);
        }
    }
}
