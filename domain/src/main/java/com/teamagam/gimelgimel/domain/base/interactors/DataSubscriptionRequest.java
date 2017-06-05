package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.observers.ResourceObserver;

public class DataSubscriptionRequest<T> implements BaseInteractor.SubscriptionRequest {

  private final Observable<T> mSource;
  private final ThreadExecutor mThreadExecutor;
  private ObservableTransformer<T, ?> mTransformer;

  private DataSubscriptionRequest(ThreadExecutor threadExecutor, Observable<T> source,
      ObservableTransformer<T, ?> transformer) {
    mThreadExecutor = threadExecutor;
    mSource = source;
    mTransformer = transformer;
  }

  public ResourceObserver subscribe() {
    return mSource.observeOn(mThreadExecutor.getScheduler())
        .compose(mTransformer)
        .subscribeWith(new SimpleObserver<>());
  }

  public static class SubscriptionRequestFactory {
    private final ThreadExecutor mThreadExecutor;

    SubscriptionRequestFactory(ThreadExecutor threadExecutor) {
      mThreadExecutor = threadExecutor;
    }

    public <T, R> DataSubscriptionRequest<?> create(Observable<T> source,
        ObservableTransformer<T, R> transformer) {
      return new DataSubscriptionRequest<>(mThreadExecutor, source, transformer);
    }
  }
}
