package com.teamagam.gimelgimel.data.message.poller;

import android.os.Handler;

import com.teamagam.gimelgimel.data.message.poller.polling.IMessagePoller;
import com.teamagam.gimelgimel.data.message.poller.strategy.BackoffStrategy;
import com.teamagam.gimelgimel.data.message.poller.strategy.RepeatedBackoffTaskRunner;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Executes message polling repeatably, using a backoff strategy, on dedicated thread
 */
public class RepeatedBackoffMessagePolling extends RepeatedBackoffTaskRunner {

//    private static Logger sLogger = LoggerFactory.create();

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

    private IMessagePoller mMessagePoller;

    @Inject
    public RepeatedBackoffMessagePolling(
            @Named("message poller") Handler handler,
            @Named("message poller") BackoffStrategy backoffStrategy,
            IMessagePoller poller) {
        super(createThreadTaskRunner(handler), backoffStrategy);
        mMessagePoller = poller;
    }

    @Override
    protected void doTask() throws IMessagePoller.ConnectionException {
        mMessagePoller.poll();
    }

    @Override
    protected void onFailedTask() {
//        sLogger.d("Message polling task failed");
//        ConnectivityStatusReceiver.broadcastNoNetwork(mContext);
    }

    @Override
    protected void onSuccessfulTask() {
//        sLogger.d("Message polling task completed successfully");
//        ConnectivityStatusReceiver.broadcastAvailableNetwork(mContext);
    }

    @Override
    protected void onSchedulingFutureTask(long delayMillis) {
//        sLogger.d("Scheduling next message polling task to execute in " + delayMillis + "ms");
    }
}
