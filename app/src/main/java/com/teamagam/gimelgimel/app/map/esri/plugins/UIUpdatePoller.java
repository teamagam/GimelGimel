package com.teamagam.gimelgimel.app.map.esri.plugins;

import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.domain.base.subscribers.DummyObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.ResourceObserver;
import java.util.concurrent.TimeUnit;

abstract class UIUpdatePoller {

  private final Scheduler mWorkScheduler;
  private ResourceObserver mObserver;
  private boolean mIsRunning;

  public UIUpdatePoller(Scheduler workScheduler) {
    mWorkScheduler = workScheduler;
    mObserver = null;
    mIsRunning = false;
  }

  public void start() {
    if (!mIsRunning) {
      mObserver = Observable.interval(Constants.UI_REFRESH_RATE_MS, TimeUnit.MILLISECONDS)
          .observeOn(mWorkScheduler)
          .doOnNext(x -> periodicalAction())
          .subscribeWith(new DummyObserver<>());
      mIsRunning = true;
    }
  }

  public void stop() {
    if (mObserver != null && !mObserver.isDisposed()) {
      mObserver.dispose();
    }
    mIsRunning = false;
  }

  protected abstract void periodicalAction();
}