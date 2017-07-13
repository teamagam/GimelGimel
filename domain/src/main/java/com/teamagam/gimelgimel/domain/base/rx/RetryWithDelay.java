package com.teamagam.gimelgimel.domain.base.rx;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.concurrent.TimeUnit;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

  private final int mMaxRetries;
  private final long mRetryDelayMillis;
  private final ThreadExecutor mThreadExecutor;
  private int mRetryCount;

  public RetryWithDelay(final int maxRetries,
      final long retryDelayMillis,
      final ThreadExecutor threadExecutor) {
    mMaxRetries = maxRetries;
    mRetryDelayMillis = retryDelayMillis;
    mThreadExecutor = threadExecutor;
    mRetryCount = 0;
  }

  @Override
  public Observable<?> apply(Observable<? extends Throwable> attempts) {
    return attempts.flatMap(new Function<Throwable, Observable<?>>() {
      @Override
      public Observable<?> apply(Throwable throwable) {
        if (++mRetryCount < mMaxRetries) {
          // When this Observable calls onNext, the original
          // Observable will be retried (i.e. re-subscribed).
          return Observable.timer(mRetryDelayMillis, TimeUnit.MILLISECONDS,
              mThreadExecutor.getScheduler());
        }

        // Max retries hit. Just pass the error along.
        return Observable.error(throwable);
      }
    });
  }
}