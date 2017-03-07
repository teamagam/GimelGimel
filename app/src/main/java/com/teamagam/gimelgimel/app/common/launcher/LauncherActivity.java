package com.teamagam.gimelgimel.app.common.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class LauncherActivity extends Activity {

    //    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_MULTIPLE = 2;
    protected GGApplication mApp;
    @Inject
    StartLocationUpdatesInteractor mStartLocationUpdatesInteractor;
    @Inject
    LocationFetcher mLocationFetcher;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            handlePermissionsIssuesThenContinue();
        } else {
            continueWithPermissionsGranted();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handlePermissionsIssuesThenContinue() {
        final List<String> neededPermissionsList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            neededPermissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            neededPermissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!neededPermissionsList.isEmpty()) {
            requestPermissions(neededPermissionsList.toArray(new String[neededPermissionsList.size()]),
                    PERMISSIONS_REQUEST_MULTIPLE);
        } else {
            continueWithPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_MULTIPLE: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                        .PERMISSION_GRANTED) {
                    continueWithPermissionsGranted();
                } else {
                    finish();
                    return;
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initSharedPreferences() {
        PreferenceManager.setDefaultValues(mApp, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(mApp, R.xml.pref_mesages, false);
    }

    private void continueWithPermissionsGranted() {
        requestGpsLocationUpdates();
        startMainActivity();
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

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        this.finish();
    }
}