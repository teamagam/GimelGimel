package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageBroadcastReceiver;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageUserLocation;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.services.GGMessagingUtils;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;

public class GGApplication extends Application {

    protected static final String TAG = "GGApplication";

    private SecuredPreferenceUtil mPrefs;
    //          TODO: clean
    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();
    private LocationFetcher mLocationFetcher;

    /**
     * Saves a boolean representing whether the app is currently started with a new version
     */
    private boolean mIsNewVersion;

    @Override
    public void onCreate() {
        super.onCreate();

        CheckIfAppUpdated();


        int serviceFrequencyMs = mPrefs.getInt(R.integer.messaging_service_polling_frequency_ms);
        int locationMinUpdatesMs = mPrefs.getInt(R.integer.location_min_update_frequency_ms);
        float locationMinDistanceM = mPrefs.getFloat(R.string.location_threshold_update_distance_m);

        mLocationFetcher = new LocationFetcher(this, (LocationManager) getSystemService(Context.LOCATION_SERVICE), new LocationFetcher.LocationFetcherListener() {
            @Override
            public void onProviderDisabled(@LocationFetcher.ProviderType String locationProvider) {
                Log.v(TAG, locationProvider + ": provider disabled.");
            }

            @Override
            public void onProviderEnabled(@LocationFetcher.ProviderType String locationProvider) {
                Log.v(TAG, locationProvider + ": provider enabled.");
            }

            @Override
            public void onNewLocationSample(LocationSample locationSample) {
                GGMessagingUtils.sendUserLocationMessageAsync(locationSample);
                Message msg = new MessageUserLocation(NetworkUtil.getMac(), locationSample);
                MessageBroadcastReceiver.sendBroadcastMessage(GGApplication.this, msg);
            }
        });
        mLocationFetcher.addProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_GPS);
//        mLocationFetcher.addProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_NETWORK);
//        mLocationFetcher.addProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_PASSIVE);
        mLocationFetcher.registerForUpdates(locationMinUpdatesMs, locationMinDistanceM);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mLocationFetcher.unregisterFromUpdates();
    }


    private void CheckIfAppUpdated() {
        // Compare current version with last saved
        int currVersion = BuildConfig.VERSION_CODE;
//        int previousVersion = getPrefs().getInt(R.string.pref_last_version_code);

        // Determine if we are using a new version
//        mIsNewVersion = currVersion > previousVersion;

        // If we have a new version
//        if (mIsNewVersion) {
        // Update to the new version in the mPrefs
//            getPrefs().applyInt(R.string.pref_last_version_code, currVersion);
//        }
    }

    /**
     * Checks if the current version is increased since the last version that was saved in mPrefs.
     *
     * @return true if version increased.
     */
    public boolean getIsNewVersion() {
        return mIsNewVersion;
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
}