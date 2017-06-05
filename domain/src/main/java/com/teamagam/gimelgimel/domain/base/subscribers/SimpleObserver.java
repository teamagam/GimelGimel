package com.teamagam.gimelgimel.domain.base.subscribers;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import io.reactivex.observers.ResourceObserver;

/**
 * Simple subscriber that does nothing on any event
 */

public class SimpleObserver<T> extends ResourceObserver<T> {

  private static final Logger sLogger =
      LoggerFactory.create(SimpleObserver.class.getSimpleName());

  @Override
  protected void onStart() {
    //request(Long.MAX_VALUE);
  }

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
