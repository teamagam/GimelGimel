package com.teamagam.gimelgimel.domain.base.subscribers;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import io.reactivex.observers.ResourceObserver;

public class ErrorLoggingObserver<T> extends ResourceObserver<T> {

  private static final Logger sLogger =
      LoggerFactory.create(ErrorLoggingObserver.class.getSimpleName());

  @Override
  public void onComplete() {
    //do nothing
  }

  @Override
  public void onError(Throwable e) {
    sLogger.e("Observable onError", e);
    //do nothing
  }

  @Override
  public void onNext(T t) {
    //do nothing
  }
}
