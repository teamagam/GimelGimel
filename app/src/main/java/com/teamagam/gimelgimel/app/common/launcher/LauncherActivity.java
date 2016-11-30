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
    protected GGApplication mApp;
    @Inject
    StartLocationUpdatesInteractor mStartLocationUpdatesInteractor;
    @Inject
    LocationFetcher mLocationFetcher;
    private AppLogger sLogger = AppLoggerFactory.create();
    private LauncherActivityComponent mLauncherAcitivtyComponent;

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
        mLauncherAcitivtyComponent = DaggerLauncherActivityComponent.builder()
                .applicationComponent(mApp.getApplicationComponent())
                .build();

        mLauncherAcitivtyComponent.inject(this);

        initSharedPreferences();

        if (isGpsGranted()) {
            requestGpsLocationUpdates();
            startMainActivity();
        } else {
            requestGpsPermission();
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
        try {
            if (!mLocationFetcher.getIsRequestingUpdates()) {
                mStartLocationUpdatesInteractor.execute();
            }
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