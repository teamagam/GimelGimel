package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DisplaySubscriptionRequest<T, R> implements BaseInteractor.SubscriptionRequest {

  private final ThreadExecutor mThreadExecutor;
  private final PostExecutionThread mPostExecutionThread;
  private final Observable<T> mSource;
  private final ObservableTransformer<T, R> mTransformer;
  private final Subscriber<R> mSubscriber;

  private DisplaySubscriptionRequest(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, Observable<T> source,
      ObservableTransformer<T, R> transformer, Subscriber<R> subscriber) {
    mThreadExecutor = threadExecutor;
    mPostExecutionThread = postExecutionThread;
    mSource = source;
    mTransformer = transformer;
    mSubscriber = subscriber;
  }

  @Override
  public Subscription subscribe() {
    Flowable.range(1, 10).subscribe(new Subscriber<Integer>() {
      @Override
      public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(Integer t) {
        System.out.println(t);
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("Done");
      }
    });
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
        ObservableTransformer<T, R> transformer, Consumer<R> subscriberOnNext) {
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
        Consumer<T> subscriberOnNext) {
      return create(source, observable -> observable, subscriberOnNext);
    }
  }
}
