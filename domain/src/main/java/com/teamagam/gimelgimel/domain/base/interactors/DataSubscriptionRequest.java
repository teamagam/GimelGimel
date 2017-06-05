package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import rx.Observable;
import rx.Subscription;

public class DataSubscriptionRequest<T> implements BaseInteractor.SubscriptionRequest {

  private final Observable<T> mSource;
  private final ThreadExecutor mThreadExecutor;
  private Observable.Transformer<T, ?> mTransformer;

  private DataSubscriptionRequest(ThreadExecutor threadExecutor,
      Observable<T> source,
      Observable.Transformer<T, ?> transformer) {
    mThreadExecutor = threadExecutor;
    mSource = source;
    mTransformer = transformer;
  }

  public Subscription subscribe() {
    return mSource.observeOn(mThreadExecutor.getScheduler())
        .compose(mTransformer)
        .subscribe(new SimpleSubscriber<>());
  }

  public static class SubscriptionRequestFactory {
    private final ThreadExecutor mThreadExecutor;

    SubscriptionRequestFactory(ThreadExecutor threadExecutor) {
      mThreadExecutor = threadExecutor;
    }

    public <T, R> DataSubscriptionRequest<?> create(Observable<T> source,
        Observable.Transformer<T, R> transformer) {
      return new DataSubscriptionRequest<>(mThreadExecutor, source, transformer);
    }
  }
}
