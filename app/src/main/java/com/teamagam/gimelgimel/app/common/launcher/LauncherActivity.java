package com.teamagam.gimelgimel.app.common.launcher;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.DaggerLauncherActivityComponent;
import com.teamagam.gimelgimel.app.injectors.components.LauncherActivityComponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.location.StartLocationUpdatesInteractor;

import javax.inject.Inject;

public class LauncherActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    @Inject
    StartLocationUpdatesInteractor mStartLocationUpdatesInteractor;
    @Inject
    LocationFetcher mLocationFetcher;
    protected GGApplication mApp;
    private AppLogger sLogger = AppLoggerFactory.create();
    private LauncherActivityComponent mLauncherActivityComponent;

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
        mLauncherActivityComponent = DaggerLauncherActivityComponent.builder()
                .applicationComponent(mApp.getApplicationComponent())
                .build();

        mLauncherActivityComponent.inject(this);

        initSharedPreferences();

        if (isGpsGranted()) {
            requestGpsLocationUpdates();
            continueAfterPermissionsCheck();
        } else {
            requestGpsPermission();
        }

    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestGpsLocationUpdates();
                } else {
                    finish();
                    return;
                }
                break;
        }

        continueAfterPermissionsCheck();
    }

    private void initSharedPreferences() {
        PreferenceManager.setDefaultValues(mApp, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(mApp, R.xml.pref_mesages, false);
    }

    private boolean isGpsGranted() {
        int gpsPermissions = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return gpsPermissions == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGpsLocationUpdates() {
        if (!mLocationFetcher.isRequestingUpdates()) {
            tryToExecuteLocationUpdatesInteractor();
        }
    }

    private void tryToExecuteLocationUpdatesInteractor() {
        try {
            mStartLocationUpdatesInteractor.execute();
        } catch (Exception ex) {
            sLogger.e("Could not register to GPS", ex);
        }
    }

    private void continueAfterPermissionsCheck() {
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        this.finish();
    }

    private void requestGpsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }
}