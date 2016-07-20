package com.teamagam.gimelgimel.app.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.receivers.NewLocationBroadcastReceiver;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;

public class LauncherActivity extends Activity {

    private Logger sLogger = LoggerFactory.create();

    protected GGApplication mApp;

    private LocationFetcher mLocationFetcher;
    private int mLocationMinUpdatesMs;
    private float mLocationMinDistanceM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }

        super.onCreate(savedInstanceState);

        mApp = (GGApplication) getApplicationContext();

        // Update the number of application launches
        mApp.getPrefs().applyInt(R.string.pref_app_launch_times,
                mApp.getPrefs().getInt(R.string.pref_app_launch_times, 0) + 1);

        mLocationMinUpdatesMs = getResources().getInteger(
                R.integer.location_min_update_frequency_ms);
        mLocationMinDistanceM = getResources().getInteger(
                R.integer.location_threshold_update_distance_m);

        mLocationFetcher = LocationFetcher.getInstance(this);

        tryAddProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_GPS);
        tryAddProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_NETWORK);
        tryAddProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_PASSIVE);

        // Request for log permissions before we use it
        if (!doesHaveGpsPermissions()) {
            LocationFetcher.askForLocationPermission(this);
        } else {
            requestGpsLocationUpdates();
            startMainActivity();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LocationFetcher.MY_PERMISSIONS_REQUEST_LOCATION:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    requestGpsLocationUpdates();
                }
                break;
        }

        startMainActivity();
    }

    private void tryAddProvider(String locationProviderGps) {
        try {
            mLocationFetcher.addProvider(locationProviderGps);
        } catch (RuntimeException ex) {
            sLogger.w("Failed adding provider " + locationProviderGps, ex);
        }
    }

    private void registerLocationReceiver() {
        mLocationFetcher.registerReceiver(new NewLocationBroadcastReceiver(
                new GGMessageSender(this)));
    }

    private void startMainActivity() {
        // Start the main activity
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(mainActivityIntent);

        this.finish();
    }

    private void requestGpsLocationUpdates() {
        try {
            if (!mLocationFetcher.getIsRequestingUpdates()) {
                mLocationFetcher.requestLocationUpdates(mLocationMinUpdatesMs, mLocationMinDistanceM);
            }
        } catch (Exception ex) {
            sLogger.e("Could not register to GPS", ex);
        }

        registerLocationReceiver();
    }

    private boolean doesHaveGpsPermissions() {
        int gpsPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        return gpsPermissions == PackageManager.PERMISSION_GRANTED;
    }
}