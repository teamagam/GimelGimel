package com.teamagam.gimelgimel.domain.base.rx;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.functions.Func1;

public class RetryWithDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

  private final int mMaxRetries;
  private final long mRetryDelayMillis;
  private int mRetryCount;

  public RetryWithDelay(final int maxRetries, final long retryDelayMillis) {
    mMaxRetries = maxRetries;
    mRetryDelayMillis = retryDelayMillis;
    mRetryCount = 0;
  }

  @Override
  public Observable<?> call(Observable<? extends Throwable> attempts) {
    return attempts.flatMap(new Func1<Throwable, Observable<?>>() {
      @Override
      public Observable<?> call(Throwable throwable) {
        if (++mRetryCount < mMaxRetries) {
          // When this Observable calls onNext, the original
          // Observable will be retried (i.e. re-subscribed).
          return Observable.timer(mRetryDelayMillis, TimeUnit.MILLISECONDS);
        }

        // Max retries hit. Just pass the error along.
        return Observable.error(throwable);
      }
    });
  }
}