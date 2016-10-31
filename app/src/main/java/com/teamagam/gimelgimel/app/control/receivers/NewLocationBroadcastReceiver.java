package com.teamagam.gimelgimel.app.control.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

/**
 * Created on 6/15/2016.
 * sends new location to the server.
 */
public class NewLocationBroadcastReceiver extends BroadcastReceiver {

    private static final Logger sLogger = LoggerFactory.create(
            NewLocationBroadcastReceiver.class);

    private GGMessageSender mMessageSender;

    public NewLocationBroadcastReceiver(GGMessageSender messageSender) {
        mMessageSender = messageSender;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNewLocationIntent(intent)) {
            LocationSample loc = intent.getParcelableExtra(
                    LocationFetcher.KEY_NEW_LOCATION_SAMPLE);
            sLogger.v("New Location: " + loc.toString());
            mMessageSender.sendUserLocationMessageAsync(loc);
        }
    }

    private boolean isNewLocationIntent(Intent intent) {
        return intent.getExtras().containsKey(LocationFetcher.KEY_NEW_LOCATION_SAMPLE);
    }
}
