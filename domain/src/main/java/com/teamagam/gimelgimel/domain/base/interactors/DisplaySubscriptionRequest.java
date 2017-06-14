package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class DisplaySubscriptionRequest<T, R> implements BaseInteractor.SubscriptionRequest {

  private final ThreadExecutor mThreadExecutor;
  private final PostExecutionThread mPostExecutionThread;
  private final Observable<T> mSource;
  private final Observable.Transformer<T, R> mTransformer;
  private final Subscriber<R> mSubscriber;

  private DisplaySubscriptionRequest(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      Observable<T> source,
      Observable.Transformer<T, R> transformer,
      Subscriber<R> subscriber) {
    mThreadExecutor = threadExecutor;
    mPostExecutionThread = postExecutionThread;
    mSource = source;
    mTransformer = transformer;
    mSubscriber = subscriber;
  }

  @Override
  public Subscription subscribe() {
    return mSource.observeOn(mThreadExecutor.getScheduler())
        .compose(mTransformer)
        .observeOn(mPostExecutionThread.getScheduler())
        .subscribe(mSubscriber);
  }

  public static class DisplaySubscriptionRequestFactory {
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    DisplaySubscriptionRequestFactory(ThreadExecutor threadExecutor,
        PostExecutionThread postExecutionThread) {
      mThreadExecutor = threadExecutor;
      mPostExecutionThread = postExecutionThread;
    }

    public <T, R> DisplaySubscriptionRequest create(Observable<T> source,
        Observable.Transformer<T, R> transformer,
        Action1<R> subscriberOnNext) {
      Subscriber<R> subscriber = new SimpleSubscriber<R>() {
        @Override
        public void onNext(R o) {
          subscriberOnNext.call(o);
        }
      };
      return new DisplaySubscriptionRequest<>(mThreadExecutor, mPostExecutionThread, source,
          transformer, subscriber);
    }

    public <T> DisplaySubscriptionRequest createSimple(Observable<T> source,
        Action1<T> subscriberOnNext) {
      return create(source, observable -> observable, subscriberOnNext);
    }
  }
}
