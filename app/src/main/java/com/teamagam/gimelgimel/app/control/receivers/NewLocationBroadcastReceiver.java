package com.teamagam.gimelgimel.app.control.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.teamagam.gimelgimel.app.common.logging.LogWrapper;
import com.teamagam.gimelgimel.app.common.logging.LogWrapperFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGMessagingUtils;

/**
 * Created on 6/15/2016.
 * sends new location to the server.
 */
public class NewLocationBroadcastReceiver extends BroadcastReceiver {

    private static final LogWrapper LOGGER = LogWrapperFactory.create(
            NewLocationBroadcastReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNewLocationIntent(intent)) {
            LocationSample loc = intent.getParcelableExtra(
                    LocationFetcher.KEY_NEW_LOCATION_SAMPLE);
            LOGGER.v("New Location: " + loc.toString());
            GGMessagingUtils.sendUserLocationMessageAsync(loc);
        }
    }

    private boolean isNewLocationIntent(Intent intent) {
        return intent.getExtras().containsKey(LocationFetcher.KEY_NEW_LOCATION_SAMPLE);
    }
}
