package com.teamagam.gimelgimel.data.message.poller;

import android.os.Handler;
import com.teamagam.gimelgimel.domain.messages.poller.IMessagePoller;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.BackoffStrategy;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.RepeatedBackoffTaskRunner;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

/**
 * Executes message polling repeatably, using a backoff strategy, on dedicated thread
 */
public class RepeatedBackoffMessagePolling extends RepeatedBackoffTaskRunner {

  //    private static Logger sLogger = LoggerFactory.create();

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
  protected Observable doTask() {
    return mMessagePoller.poll();
  }

  @Override
  protected void onFailedTask(Throwable throwable) {
    //        sLogger.d("Message polling task failed");
    //        ConnectivityStatusReceiver.broadcastNoNetwork(mContext);
    mDataConnectivityRepository.setStatus(ConnectivityStatus.DISCONNECTED);
  }

  @Override
  protected void onSuccessfulTask(Object obj) {
    //        sLogger.d("Message polling task completed successfully");
    //        ConnectivityStatusReceiver.broadcastAvailableNetwork(mContext);
    mDataConnectivityRepository.setStatus(ConnectivityStatus.CONNECTED);
  }

  @Override
  protected void onSchedulingFutureTask(long delayMillis) {
    //        sLogger.d("Scheduling next message polling task to execute in " + delayMillis + "ms");
  }
}
