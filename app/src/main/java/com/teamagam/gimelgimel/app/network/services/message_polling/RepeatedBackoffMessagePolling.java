package com.teamagam.gimelgimel.app.network.services.message_polling;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.app.common.BackoffStrategy;
import com.teamagam.gimelgimel.app.common.ExponentialBackoffStrategy;
import com.teamagam.gimelgimel.app.common.RepeatedBackoffTaskRunner;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.network.services.message_polling.poller.IMessageBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.poller.IMessagePoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.poller.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.app.network.services.message_polling.poller.MessageLocalBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.poller.MessageLongPoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.poller.PolledMessagesBroadcaster;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

/**
 * Executes message polling repeatably, using a backoff strategy, on dedicated thread
 */
public class RepeatedBackoffMessagePolling extends RepeatedBackoffTaskRunner {

    private static Logger sLogger = LoggerFactory.create();
    private static HandlerThread sHandlerThread = new HandlerThread("MessagePolling");

    public static RepeatedBackoffMessagePolling create(Context context) {
        IMessagePoller poller = createPoller(context);
        BackoffStrategy backoffStrategy = createBackoffStrategy();

        return new RepeatedBackoffMessagePolling(sHandlerThread, backoffStrategy, poller, context);
    }

    private static BackoffStrategy createBackoffStrategy() {
        return new ExponentialBackoffStrategy(
                Constants.POLLING_EXP_BACKOFF_BASE_INTERVAL_MILLIS,
                Constants.POLLING_EXP_BACKOFF_MULTIPLIER,
                Constants.POLLING_EXP_BACKOFF_MAX_BACKOFF_MILLIS);
    }

    private static IMessagePoller createPoller(Context context) {
        IMessageBroadcaster messageLocalBroadcaster = new MessageLocalBroadcaster(context);
        IPolledMessagesProcessor processor = new PolledMessagesBroadcaster(messageLocalBroadcaster);
        PreferenceUtil prefUtil = new PreferenceUtil(context.getResources(),
                PreferenceManager.getDefaultSharedPreferences(context));
        return new MessageLongPoller(RestAPI.getInstance().getMessagingAPI(), processor,
                prefUtil);
    }

    private static TaskRunner createThreadTaskRunner(HandlerThread handlerThread) {
        final Handler handler = new Handler(getLooper(handlerThread));
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

    private static Looper getLooper(HandlerThread handlerThread) {
        Looper looper = handlerThread.getLooper();

        if (looper == null) {
            handlerThread.start();
            looper = handlerThread.getLooper();
        }

        return looper;
    }

    private IMessagePoller mMessagePoller;
    private Context mContext;

    private RepeatedBackoffMessagePolling(
            HandlerThread handlerThread,
            BackoffStrategy backoffStrategy,
            IMessagePoller poller,
            Context context) {
        super(createThreadTaskRunner(handlerThread), backoffStrategy);
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
