package com.teamagam.gimelgimel.domain.base.executor;

import com.teamagam.gimelgimel.domain.base.interactors.AbstractInteractor;

import rx.Scheduler;

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute the
 * {@link AbstractInteractor} out of the UI thread.
 */
public interface ThreadExecutor{
    Scheduler getScheduler();
}
