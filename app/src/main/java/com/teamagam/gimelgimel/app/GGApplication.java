package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.network.services.GGMessagePollingService;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
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

        int serviceFrequencyMs = getResources().getInteger(
                R.integer.messaging_service_polling_frequency_ms);

        GGMessagePollingService.startMessagePollingPeriodically(this, serviceFrequencyMs);


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
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