package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public abstract class BaseInteractor implements Interactor {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final Collection<Subscription> mSubscriptions;

    public BaseInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mSubscriptions = new ArrayList<>();
    }


    @Override
    public void execute() {
        Iterable<SubscriptionRequest> subscriptionRequests = buildSubscriptionRequests();
        for (SubscriptionRequest se : subscriptionRequests) {
            subscribe(se);
        }
    }

    @Override
    public void unsubscribe() {
        for (Subscription sub : mSubscriptions) {
            if (sub != null && !sub.isUnsubscribed()) {
                sub.unsubscribe();
            }
        }
    }

    protected abstract Iterable<SubscriptionRequest> buildSubscriptionRequests();

    private void subscribe(SubscriptionRequest se) {
        Subscription subscription = se.subscribe(mThreadExecutor, mPostExecutionThread);
        mSubscriptions.add(subscription);
    }

    protected static class SubscriptionRequest<T> {

        private Observable<T> mObservable;
        private Subscriber<T> mSubscriber;

        protected SubscriptionRequest(Observable<T> observable) {
            mObservable = observable;
        }

        protected SubscriptionRequest(Observable<T> observable, Subscriber<T> subscriber) {
            mObservable = observable;
            mSubscriber = subscriber;
        }

        protected SubscriptionRequest(Observable<T> observable, Action1<T> subscriberOnNext) {
            this(observable);
            mSubscriber = new SimpleSubscriber<T>() {
                @Override
                public void onNext(T o) {
                    subscriberOnNext.call(o);
                }
            };
        }

        private Subscription subscribe(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
            Subscription subscription;

            Observable<T> obs = mObservable.subscribeOn(threadExecutor.getScheduler());
            if (mSubscriber != null) {
                subscription = obs.subscribe();
            } else {
                subscription = obs
                        .observeOn(postExecutionThread.getScheduler())
                        .subscribe(mSubscriber);
            }

            return subscription;
        }
    }
}
