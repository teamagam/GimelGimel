package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.DummyObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;

public class DisplaySubscriptionRequest<T, R> implements BaseInteractor.SubscriptionRequest {

  private final ThreadExecutor mThreadExecutor;
  private final PostExecutionThread mPostExecutionThread;
  private final Observable<T> mSource;
  private final ObservableTransformer<T, R> mTransformer;
  private final ResourceObserver<R> mObserver;

  private DisplaySubscriptionRequest(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      Observable<T> source,
      ObservableTransformer<T, R> transformer,
      ResourceObserver<R> observer) {
    mThreadExecutor = threadExecutor;
    mPostExecutionThread = postExecutionThread;
    mSource = source;
    mTransformer = transformer;
    mObserver = observer;
  }

  @Override
  public ResourceObserver subscribe() {
    return mSource.observeOn(mThreadExecutor.getScheduler())
        .compose(mTransformer)
        .observeOn(mPostExecutionThread.getScheduler())
        .subscribeWith(mObserver);
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
        ObservableTransformer<T, R> transformer,
        Consumer<R> subscriberOnNext) {
      ResourceObserver<R> observer = new DummyObserver<R>() {
        @Override
        public void onNext(R o) {
          try {
            subscriberOnNext.accept(o);
          } catch (Exception e) {
            this.onError(e);
          }
        }
      };
      return new DisplaySubscriptionRequest<>(mThreadExecutor, mPostExecutionThread, source,
          transformer, observer);
    }

    public <T> DisplaySubscriptionRequest createSimple(Observable<T> source,
        Consumer<T> subscriberOnNext) {
      return create(source, observable -> observable, subscriberOnNext);
    }
  }
}
