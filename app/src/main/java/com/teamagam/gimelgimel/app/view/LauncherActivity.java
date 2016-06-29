package com.teamagam.gimelgimel.app.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.control.receivers.NewLocationBroadcastReceiver;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;

public class LauncherActivity extends Activity {

    protected final String TAG = ((Object) this).getClass().getSimpleName();

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

        try {
            mLocationFetcher.addProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_GPS);
            mLocationFetcher.addProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_NETWORK);
            mLocationFetcher.addProvider(LocationFetcher.ProviderType.LOCATION_PROVIDER_PASSIVE);
            mLocationFetcher.requestLocationUpdates(mLocationMinUpdatesMs, mLocationMinDistanceM);

            //if the registration was OK and no SecurityException was thrown
            registerLocationReceiver();
            startMainActivity();
        } catch (SecurityException e) {
            LocationFetcher.askForLocationPermission(this);
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
                    mLocationFetcher.requestLocationUpdates(mLocationMinUpdatesMs,
                            mLocationMinDistanceM);
                    registerLocationReceiver();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            // other 'case' lines to check for other
            // permissions this app might request
            default:
        }

        startMainActivity();
    }

    private void registerLocationReceiver() {
        mLocationFetcher.registerReceiver(new NewLocationBroadcastReceiver(
                new GGMessageSender(this)));
    }

    private void startMainActivity() {
        // Start the main activity
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}