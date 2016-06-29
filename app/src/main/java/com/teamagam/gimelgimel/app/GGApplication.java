package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.receivers.GpsStatusBroadcastReceiver;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;

public class GGApplication extends Application {

    private SecuredPreferenceUtil mPrefs;
    private GpsStatusBroadcastReceiver mGpsStatusBroadcastReceiver;
    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();


    @Override
    public void onCreate() {
        super.onCreate();

        init();
        registerBroadcasts();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        unregisterBroadcasts();
    }

    public SecuredPreferenceUtil getPrefs() {
        if (mPrefs == null) {
            // Set up a preferences manager (with basic security)
            mPrefs = new SecuredPreferenceUtil(getResources(),
                    PreferenceManager.getDefaultSharedPreferences(this),
                    new BasicStringSecurity(mPrefSecureKey));
        }

        return mPrefs;
    }

    private void init() {
        mGpsStatusBroadcastReceiver = new GpsStatusBroadcastReceiver(this);

        LoggerFactory.init(this);

        // Initialize the fresco plugin.
        // Should be here instead of each activity
        Fresco.initialize(this);
    }

    private void registerBroadcasts() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter(GpsStatusBroadcastReceiver.BROADCAST_GPS_STATUS_ACTION);

        localBroadcastManager.registerReceiver(mGpsStatusBroadcastReceiver, intentFilter);
    }

    private void unregisterBroadcasts() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

        localBroadcastManager.unregisterReceiver(mGpsStatusBroadcastReceiver);
    }
}