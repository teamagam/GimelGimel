package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.network.services.message_polling.IMessageBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.IMessagePoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.app.network.services.message_polling.MessageLocalBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.MessageLongPoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.PolledMessagesBroadcaster;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * Polls for new messages from remote service on request.
 */
public class GGMessageLongPollingService extends IntentService {

    private static void setShouldPollPreferenceFlag(Context context, boolean value) {
        PreferenceUtil prefUtil = new PreferenceUtil(context.getResources(),
                PreferenceManager.getDefaultSharedPreferences(context));
        prefUtil.applyBoolean(R.string.pref_should_long_poll_messages, value);
    }

    private static void startActionMessagePolling(Context context) {
        Intent intent = new Intent(context, GGMessageLongPollingService.class);
        intent.setAction(ACTION_MESSAGE_POLLING);
        context.startService(intent);
    }

    /**
     * Initiate an intent to perform a message polling action.<br/> Message polling will
     * run repeatedly until {@link GGMessageLongPollingService#stopMessagePollingAsync}
     * is called
     *
     * @param context - to be used to construct every new action intent
     */
    public static void startMessageLongPollingAsync(final Context context) {
        setShouldPollPreferenceFlag(context, true);
        GGMessageLongPollingService.startActionMessagePolling(context);
    }

    /**
     * Stops the service from future message polls.<br/>
     * The current poll will <b>not</b> be interrupted!
     */
    public static void stopMessagePollingAsync(Context context) {
        setShouldPollPreferenceFlag(context, false);
    }

    private static final String ACTION_MESSAGE_POLLING =
            "com.teamagam.gimelgimel.app.network.services.action.MESSAGE_POLLING";

    private static final Logger sLogger = LoggerFactory.create(
            GGMessageLongPollingService.class);

    private PreferenceUtil mPrefUtil;

    private IMessagePoller mPoller;


    public GGMessageLongPollingService() {
        super(GGMessageLongPollingService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IMessageBroadcaster messageLocalBroadcaster = new MessageLocalBroadcaster(this);
        IPolledMessagesProcessor processor = new PolledMessagesBroadcaster(messageLocalBroadcaster);
        mPrefUtil = new PreferenceUtil(getResources(),
                PreferenceManager.getDefaultSharedPreferences(this));
        mPoller = new MessageLongPoller(RestAPI.getInstance().getMessagingAPI(), processor,
                mPrefUtil);
    }

    /**
     * Executes on new intent received by service.
     * Maps between given intent action and its appropriate action.
     *
     * @param intent - injected externally by Android with generating intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (isMessagePollingIntent(intent)) {
            poll();
            if (shouldContinuePolling()) {
                startActionMessagePolling(this);
            }
        }
    }

    private boolean isMessagePollingIntent(Intent intent) {
        return intent != null && ACTION_MESSAGE_POLLING.equals(intent.getAction());
    }

    private void poll() {
        try {
            mPoller.poll();
            ConnectivityStatusReceiver.broadcastAvailableNetwork(this);
        } catch (IMessagePoller.ConnectionException ex) {
            sLogger.w("Polling messages failed due to a connection problem", ex);
            ConnectivityStatusReceiver.broadcastNoNetwork(this);
        } catch (Exception ex) {
            sLogger.e("Error polling", ex);
        }
    }

    private boolean shouldContinuePolling() {
        return mPrefUtil.getBoolean(R.string.pref_should_long_poll_messages, true);
    }
}
