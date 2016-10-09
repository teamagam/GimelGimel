package com.teamagam.gimelgimel.domain.messages.poller.strategy;

import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

import rx.Observable;

/**
 * Runs given task periodically in a background thread
 */
public abstract class RepeatedBackoffTaskRunner<T> {

    private final BackoffStrategy mBackoffStrategy;
    private final TaskRunner mTaskRunner;
    private Runnable mBackoffRepeatingTask;
    private boolean mIsRunning;

    public RepeatedBackoffTaskRunner(TaskRunner taskRunner, BackoffStrategy backoffStrategy) {
        mTaskRunner = taskRunner;
        mBackoffStrategy = backoffStrategy;
        mIsRunning = false;
    }

    public void start() {
        if (mIsRunning) {
            throw new RuntimeException("Task is already running!");
        }
        mBackoffRepeatingTask = createBackoffRepeatingTask();

        mTaskRunner.runNow(mBackoffRepeatingTask);

        mIsRunning = true;
    }

    public void stopNextExecutions() {
        mTaskRunner.stopFutureRuns(mBackoffRepeatingTask);
        mIsRunning = false;
    }

    protected abstract Observable<T> doTask();

    protected void onSuccessfulTask(T t) {
        //Empty stub. Override if needed
    }

    protected void onFailedTask(Throwable throwable) {
        //Empty stub. Override if needed
    }

    protected void onSchedulingFutureTask(long delayMillis) {
        //Empty stub. Override if needed
    }

    private Runnable createBackoffRepeatingTask() {
        return new Runnable() {
            @Override
            public void run() {
                doTask()
                        .doOnCompleted(() ->
                                futureScheduleIfNeeded(mBackoffRepeatingTask, mBackoffStrategy.getBackoffMillis()))
                        .subscribe(new BackoffTaskSubscriber());
            }

            private void futureScheduleIfNeeded(Runnable futureTask, long delayMillis) {
                if (mIsRunning) {
                    mTaskRunner.runInFuture(futureTask, delayMillis);
                    onSchedulingFutureTask(delayMillis);
                }
            }
        };
    }

    public interface TaskRunner {
        void runNow(Runnable task);

        void runInFuture(Runnable task, long delayMillis);

        void stopFutureRuns(Runnable task);
    }


    private class BackoffTaskSubscriber extends SimpleSubscriber<T> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mBackoffStrategy.increase();
            onFailedTask(e);
        }

        @Override
        public void onNext(T t) {
            super.onNext(t);
            mBackoffStrategy.reset();
            onSuccessfulTask(t);
        }
    }
}