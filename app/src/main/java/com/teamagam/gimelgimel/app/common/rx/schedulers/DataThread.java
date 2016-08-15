package com.teamagam.gimelgimel.app.common.rx.schedulers;

import com.teamagam.gimelgimel.domain.executor.ThreadExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * DataThread implementation based on a {@link rx.Scheduler}
 * which will execute actions on the Android UI thread
 */
@Singleton
public class DataThread implements ThreadExecutor{

    @Inject
    public DataThread () {}

    @Override
    public void execute(Runnable command) {
//        Schedulers.io().createWorker().
        new Thread(command).start();
    }
}

