package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.utils.GsonUtil;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * Polls for new messages from remote service on request.
 */
public class GGMessagePollingService extends IntentService {

    public static final String LOG_TAG = GGMessagePollingService.class.getSimpleName();

    private static final String ACTION_MESSAGE_POLLING =
            "com.teamagam.gimelgimel.app.network.services.action.MESSAGE_POLLING";

    public GGMessagePollingService() {
        super("GGMessagePollingService");
    }


    /**
     * Starts this service to perform message polling action. If the service is
     * already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionMessagePolling(Context context) {
        Intent intent = new Intent(context, GGMessagePollingService.class);
        intent.setAction(ACTION_MESSAGE_POLLING);
        context.startService(intent);
    }

    /**
     * Sets this service to periodically start performing message polling action
     *
     * @param context - to be used to construct every new action intent
     * @param period  - amount of time in milliseconds between subsequent executions.
     */
    public static void startMessagePollingPeriodically(final Context context, long period) {
        Timer t = new Timer("pollingTimer", true /*isDaemon*/);

        TimerTask pollingTask = new TimerTask() {
            @Override
            public void run() {
                GGMessagePollingService.startActionMessagePolling(context);
            }
        };

        t.scheduleAtFixedRate(pollingTask, 0, period);
    }

    /**
     * Executes on new intent received by service.
     * Maps between given intent action and its appropriate action.
     *
     * @param intent - injected externally by Android with generating intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MESSAGE_POLLING.equals(action)) {
                handleActionMessagePolling();
            }
        }
    }

    /**
     * Handle message polling action in a background thread (provided by the
     * {@link IntentService} class).
     */
    private void handleActionMessagePolling() {
        PreferenceUtil prefUtils = new PreferenceUtil(getResources(),
                PreferenceManager.getDefaultSharedPreferences(this));
        //get latest synchronized date from shared prefs
        long synchronizedDateMs = prefUtils.getLong(
                R.string.pref_latest_received_message_date_in_ms, 0);

        Log.v(LOG_TAG,
                "Polling for new messages with synchronization date (ms): " + synchronizedDateMs);

        Collection<Message> messages = GGMessagingUtils.getMessagesSync(synchronizedDateMs);

        if (messages == null || messages.size() == 0) {
            Log.v(LOG_TAG, "No new messages available");
            return;
        }

        processNewMessages(messages);

        Message maximumMessageDateMessage = getMaximumDateMessage(messages);
        long newSynchronizedDateMs = maximumMessageDateMessage.getCreatedAt().getTime();

        //Get latest date and write to shared preference for future polling
        prefUtils.commitLong(R.string.pref_latest_received_message_date_in_ms,
                newSynchronizedDateMs);
        Log.v(LOG_TAG, "New synchronization date (ms) set to " + newSynchronizedDateMs);
    }


    /**
     * Process given messages
     *
     * @param messages - messages to process
     */
    private void processNewMessages(Collection<Message> messages) {
        Log.v(LOG_TAG, "MessagePolling service processing " + messages.size() + " new messages");

        for (Message msg :
                messages) {
            if (msg.getSenderId().equals(NetworkUtil.getMac())) {
                continue;
            }
            Intent intent = new Intent(msg.getClass().getName());
            intent.putExtra(MessageBroadcastReceiver.MESSAGE, GsonUtil.toJson(msg));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private Message getMaximumDateMessage(Collection<Message> messages) {
        Message m = Collections.max(messages, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.getCreatedAt().compareTo(rhs.getCreatedAt());
            }
        });

        return m;
    }
}
