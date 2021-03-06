package com.teamagam.gimelgimel.app.common.rx.schedulers;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * DataThread implementation based on a {@link rx.Scheduler}
 * which will execute actions on the Android UI thread
 */
@Singleton
public class DataThread implements ThreadExecutor {

  @Inject
  public DataThread() {
  }

  @Override
  public Scheduler getScheduler() {
    return Schedulers.io();
  }
}

