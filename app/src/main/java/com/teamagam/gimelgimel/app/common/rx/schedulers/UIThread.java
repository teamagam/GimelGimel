package com.teamagam.gimelgimel.app.common.rx.schedulers;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * MainThread (UI Thread) implementation based on a {@link rx.Scheduler}
 * which will execute actions on the Android UI thread
 */
@Singleton
public class UIThread implements PostExecutionThread {

    @Inject
    public UIThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
