package com.teamagam.gimelgimel.app.network.services.message_polling;

import android.content.Context;
import android.os.Handler;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.BackoffStrategy;
import com.teamagam.gimelgimel.app.common.ExponentialBackoffStrategy;
import com.teamagam.gimelgimel.app.common.RepeatedBackoffTaskRunner;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.IMessageBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.IMessagePoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.MessageLocalBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.MessageLongPoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.polling.PolledMessagesBroadcaster;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;

/**
 * Executes message polling repeatably, using a backoff strategy, on dedicated thread
 */
public class RepeatedBackoffMessagePolling extends RepeatedBackoffTaskRunner {

    private static Logger sLogger = LoggerFactory.create();

    public static RepeatedBackoffMessagePolling create(GGApplication ggApplication) {
        IMessagePoller poller = createPoller(ggApplication);
        BackoffStrategy backoffStrategy = createBackoffStrategy();

        return new RepeatedBackoffMessagePolling(ggApplication.getMessagingHandler(),
                backoffStrategy, poller,
                ggApplication);
    }

    private static BackoffStrategy createBackoffStrategy() {
        return new ExponentialBackoffStrategy(
                Constants.POLLING_EXP_BACKOFF_BASE_INTERVAL_MILLIS,
                Constants.POLLING_EXP_BACKOFF_MULTIPLIER,
                Constants.POLLING_EXP_BACKOFF_MAX_BACKOFF_MILLIS);
    }

    private static IMessagePoller createPoller(GGApplication ggApplication) {
        IMessageBroadcaster messageLocalBroadcaster = new MessageLocalBroadcaster(ggApplication);
        IPolledMessagesProcessor processor = new PolledMessagesBroadcaster(messageLocalBroadcaster);
        SecuredPreferenceUtil spu = ggApplication.getPrefs();
        return new MessageLongPoller(RestAPI.getInstance().getMessagingAPI(), processor, spu);
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

    private IMessagePoller mMessagePoller;
    private Context mContext;

    private RepeatedBackoffMessagePolling(
            Handler handler,
            BackoffStrategy backoffStrategy,
            IMessagePoller poller,
            Context context) {
        super(createThreadTaskRunner(handler), backoffStrategy);
        mContext = context;
        mMessagePoller = poller;
    }

    @Override
    protected void doTask() throws IMessagePoller.ConnectionException {
        mMessagePoller.poll();
    }

    @Override
    protected void onFailedTask() {
        sLogger.d("Message polling task failed");
        ConnectivityStatusReceiver.broadcastNoNetwork(mContext);
    }

    @Override
    protected void onSuccessfulTask() {
        sLogger.d("Message polling task completed successfully");
        ConnectivityStatusReceiver.broadcastAvailableNetwork(mContext);
    }

    @Override
    protected void onSchedulingFutureTask(long delayMillis) {
        sLogger.d("Scheduling next message polling task to execute in " + delayMillis + "ms");
    }
}
