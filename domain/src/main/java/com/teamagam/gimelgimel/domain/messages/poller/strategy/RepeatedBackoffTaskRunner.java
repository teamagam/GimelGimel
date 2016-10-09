package com.teamagam.gimelgimel.domain.messages.poller.strategy;

/**
 * Runs given task periodically in a background thread
 */
public abstract class RepeatedBackoffTaskRunner {

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

    protected abstract void doTask() throws Exception;

    protected void onSuccessfulTask() {
        //Empty stub. Override if needed
    }

    protected void onFailedTask() {
        //Empty stub. Override if needed
    }

    protected void onSchedulingFutureTask(long delayMillis) {
        //Empty stub. Override if needed
    }

    private Runnable createBackoffRepeatingTask() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    doTask();
                    mBackoffStrategy.reset();
                    onSuccessfulTask();
                } catch (Exception ex) {
                    mBackoffStrategy.increase();
                    onFailedTask();
                }

                if (mIsRunning) {
                    futureSchedule(mBackoffRepeatingTask, mBackoffStrategy.getBackoffMillis());
                }
            }

            private void futureSchedule(Runnable futureTask, long delayMillis) {
                mTaskRunner.runInFuture(futureTask, delayMillis);
                onSchedulingFutureTask(delayMillis);
            }
        };
    }

    public interface TaskRunner {
        void runNow(Runnable task);

        void runInFuture(Runnable task, long delayMillis);

        void stopFutureRuns(Runnable task);
    }
}
