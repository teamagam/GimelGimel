package com.teamagam.gimelgimel.domain.messages.poller.strategy;

import com.teamagam.gimelgimel.domain.base.subscribers.DummyObserver;
import io.reactivex.Observable;

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
    return () -> doTask().subscribe(new BackoffTaskObserver());
  }

  public interface TaskRunner {
    void runNow(Runnable task);

    void runInFuture(Runnable task, long delayMillis);

    void stopFutureRuns(Runnable task);
  }

  private class BackoffTaskObserver extends DummyObserver<T> {
    @Override
    public void onError(Throwable e) {
      mBackoffStrategy.increase();
      onFailedTask(e);
      onComplete();
    }

    @Override
    public void onNext(T t) {
      mBackoffStrategy.reset();
      onSuccessfulTask(t);
    }

    @Override
    public void onComplete() {
      futureScheduleIfNeeded(mBackoffRepeatingTask, mBackoffStrategy.getBackoffMillis());
    }

    private void futureScheduleIfNeeded(Runnable futureTask, long delayMillis) {
      if (mIsRunning) {
        mTaskRunner.runInFuture(futureTask, delayMillis);
        onSchedulingFutureTask(delayMillis);
      }
    }
  }
}