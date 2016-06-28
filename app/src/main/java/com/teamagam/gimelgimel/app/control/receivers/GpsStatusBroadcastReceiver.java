package com.teamagam.gimelgimel.app.control.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.teamagam.gimelgimel.app.common.ConsistentStatusEventRaiser;

/**
 * Created by CV on 6/27/2016.
 */
public class GpsStatusBroadcastReceiver extends BroadcastReceiver implements ConsistentStatusEventRaiser.EventAction<Boolean> {

    /**
     * An intent action name, that the receiver should register to
     */
    public static final String BROADCAST_GPS_STATUS_ACTION = "newGpsStatus";

    /**
     * An intent action name, that the BL class should register to
     */
    public static final String BROADCAST_NEW_GPS_STATUS_ACTION = "broadCastNewGpsStatus";

    /**
     * The extra string key
     */
    public static final String GPS_STATUS_EXTRA = "gpsStatusExtra";

    public static void broadcastGpsStatus(Context context, boolean gpsStatus) {
        Intent intent = new Intent(BROADCAST_GPS_STATUS_ACTION);
        intent.putExtra(GPS_STATUS_EXTRA, gpsStatus);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private Context mApplicationContext;
    private ConsistentStatusEventRaiser<Boolean> mEventRaiser;

    public GpsStatusBroadcastReceiver(Context applicationContext)  {
        mApplicationContext = applicationContext;

        // TODO: Config
        mEventRaiser = new ConsistentStatusEventRaiser<>(10000, true, this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null) {
            boolean gpsStatus = intent.getBooleanExtra(GPS_STATUS_EXTRA, true);

            mEventRaiser.updateStatus(gpsStatus);
        }
    }

    @Override
    public void Do(Boolean gpsStatus) {
        Intent broadcastIntent = createIntent(gpsStatus);
        broadcastGpsStatus(mApplicationContext, broadcastIntent);
    }

    private Intent createIntent(Boolean gpsStatus) {
        Intent broadcastIntent = new Intent(BROADCAST_NEW_GPS_STATUS_ACTION);
        broadcastIntent.putExtra(GPS_STATUS_EXTRA, gpsStatus);

        return broadcastIntent;
    }

    /**
     * Broadcasts the intent to the BL class
     * @param context
     * @param intent
     */
    private void broadcastGpsStatus(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
