package com.gimelgimel.domain.executor;

import rx.Scheduler;

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute the
 * {@link com.gimelgimel.domain.interactors.base.AbstractInteractor} out of the UI thread.
 */
public interface ThreadExecutor {
    Scheduler getScheduler();
}
