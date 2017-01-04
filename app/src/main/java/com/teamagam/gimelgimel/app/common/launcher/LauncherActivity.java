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
import com.teamagam.gimelgimel.app.location.TurnOnGpsDialogFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.location.StartLocationUpdatesInteractor;

import javax.inject.Inject;

public class LauncherActivity extends Activity implements TurnOnGpsDialogFragment.TurnOnGpsDialogListener {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    protected GGApplication mApp;
    @Inject
    StartLocationUpdatesInteractor mStartLocationUpdatesInteractor;
    @Inject
    LocationFetcher mLocationFetcher;
    @Inject
    Navigator mNavigator;
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
        requestGpsLocationUpdates();
        makeSureGpsIsOn();
        startMainActivity();
    }

    private void makeSureGpsIsOn() {
        if (!mLocationFetcher.isGpsProviderEnabled()) {
            mNavigator.navigateToTurnOnGPSDialog(this, this);
        }
    }

    private void initSharedPreferences() {
        PreferenceManager.setDefaultValues(mApp, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(mApp, R.xml.pref_mesages, false);
    }

    /**
     * opens dialog for permission from the user.
     * For API 23 or higher.
     */
    public void requestGpsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    requestGpsLocationUpdates();
                }
                break;
        }
    }

    @Override
    public void onEnableGpsClick() {
        startMainActivity();
    }

    @Override
    public void onDoNotEnableGpsClick() {
        startMainActivity();
    }

    private void startMainActivity() {
        // Start the main activity
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(mainActivityIntent);

        this.finish();
    }

    private void requestGpsLocationUpdates() {
        if (!isGpsGranted()) {
            requestGpsPermission();
        } else if (!mLocationFetcher.isRequestingUpdates()) {
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

    private boolean isGpsGranted() {
        int gpsPermissions = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return gpsPermissions == PackageManager.PERMISSION_GRANTED;
    }
}