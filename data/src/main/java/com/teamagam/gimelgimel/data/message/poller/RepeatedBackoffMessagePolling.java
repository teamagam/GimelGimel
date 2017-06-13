package com.teamagam.gimelgimel.data.message.poller;

import android.os.Handler;
import com.teamagam.gimelgimel.domain.messages.poller.IMessagePoller;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.BackoffStrategy;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.RepeatedBackoffTaskRunner;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import io.reactivex.Flowable;
import javax.inject.Inject;
import javax.inject.Named;

public class RepeatedBackoffMessagePolling extends RepeatedBackoffTaskRunner {

  private final ConnectivityStatusRepository mDataConnectivityRepository;
  private final IMessagePoller mMessagePoller;

  @Inject
  public RepeatedBackoffMessagePolling(
      @Named("message poller")
          Handler handler,
      @Named("message poller")
          BackoffStrategy backoffStrategy,
      @Named("data")
          ConnectivityStatusRepository dataConnectivityRepo, IMessagePoller poller) {
    super(createThreadTaskRunner(handler), backoffStrategy);
    mDataConnectivityRepository = dataConnectivityRepo;
    mMessagePoller = poller;
  }

  private static TaskRunner createThreadTaskRunner(final Handler handler) {
    return new TaskRunner() {
      @Override
      public void runNow(Runnable task) {
        handler.post(task);
      }

      @Override
      public void runInFuture(Runnable task, long delayMillis) {
        handler.postDelayed(task, delayMillis);
      }

      @Override
      public void stopFutureRuns(Runnable task) {
        handler.removeCallbacks(task);
      }
    };
  }

  @Override
  protected Flowable doTask() {
    return mMessagePoller.poll();
  }

  @Override
  protected void onFailedTask(Throwable throwable) {
    mDataConnectivityRepository.setStatus(ConnectivityStatus.DISCONNECTED);
  }

  @Override
  protected void onSuccessfulTask(Object obj) {
    mDataConnectivityRepository.setStatus(ConnectivityStatus.CONNECTED);
  }

  @Override
  protected void onSchedulingFutureTask(long delayMillis) {
  }
}
