package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.network.services.message_polling.IMessageBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.IMessagePoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.app.network.services.message_polling.MessageLocalBroadcaster;
import com.teamagam.gimelgimel.app.network.services.message_polling.MessagePoller;
import com.teamagam.gimelgimel.app.network.services.message_polling.PolledMessagesBroadcaster;
import com.teamagam.gimelgimel.app.utils.PreferenceUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * Polls for new messages from remote service on request.
 */
public class GGMessageLongPollingService extends IntentService {

    public static final String LOG_TAG = GGMessageLongPollingService.class.getSimpleName();

    private static final String ACTION_MESSAGE_POLLING =
            "com.teamagam.gimelgimel.app.network.services.action.MESSAGE_POLLING";
    private PreferenceUtil mPrefUtil;

    public GGMessageLongPollingService() {
        super("GGMessagePollingService");
    }

    private IMessagePoller mPoller;


    private static void setShouldPollPrefernceFlag(Context context, boolean value){
        PreferenceUtil prefUtil = new PreferenceUtil(context.getResources(),
                PreferenceManager.getDefaultSharedPreferences(context));
        prefUtil.commitBoolean(R.string.pref_should_poll, value);
    }
    /**
     * Starts this service to perform message polling action. If the service is
     * already performing a task this action will be queued.
     *
     * @see IntentService
     */
    private static void startActionMessagePolling(Context context) {
        Intent intent = new Intent(context, GGMessageLongPollingService.class);
        intent.setAction(ACTION_MESSAGE_POLLING);
        context.startService(intent);
    }


    /**
     * Sets this service to periodically start performing message polling action
     * Message polling will run repeatedly until {@link GGMessageLongPollingService#stopMessagePolling}
     * will be called
     *
     * @param context - to be used to construct every new action intent
     */
    public static void startMessageLongPolling(final Context context) {
        setShouldPollPrefernceFlag(context, true);
        GGMessageLongPollingService.startActionMessagePolling(context);
    }

    /**
     * Stops the service from future message polls.<br/>
     * The current poll will <b>not</b> be interrupted!
     */
    public static void stopMessagePolling(Context context) {
        setShouldPollPrefernceFlag(context, false);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IMessageBroadcaster messageLocalBroadcaster = new MessageLocalBroadcaster(this);
        IPolledMessagesProcessor processor = new PolledMessagesBroadcaster(messageLocalBroadcaster);
        mPrefUtil = new PreferenceUtil(getResources(),
                PreferenceManager.getDefaultSharedPreferences(this));
        mPoller = new MessagePoller(RestAPI.getInstance().getMessagingAPI(), processor, mPrefUtil);
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
                mPoller.poll();

                //Should repeatedly call polling as long as flag is true
                if (mPrefUtil.getBoolean(R.string.pref_should_poll, true)) {
                    startActionMessagePolling(this);
                }
            }
        }
    }
}
