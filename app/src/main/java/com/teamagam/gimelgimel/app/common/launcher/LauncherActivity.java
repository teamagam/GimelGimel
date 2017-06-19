package com.teamagam.gimelgimel.app.common.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerLauncherActivityComponent;
import com.teamagam.gimelgimel.app.injectors.components.LauncherActivityComponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.notifications.AppNotifier;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.location.StartLocationUpdatesInteractor;
import com.teamagam.gimelgimel.domain.notifications.NotifyOnNewMessageInteractorFactory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LauncherActivity extends Activity {

  private static final int PERMISSIONS_REQUEST_CODE_MULTIPLE = 2;
  private static final String[] NEEDED_PERMISSIONS =
      { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };
  protected GGApplication mApp;
  @Inject
  StartLocationUpdatesInteractor mStartLocationUpdatesInteractor;
  @Inject
  NotifyOnNewMessageInteractorFactory mNotifyOnNewMessageInteractorFactory;
  @Inject
  LocationFetcher mLocationFetcher;
  @Inject
  AppNotifier mAppNotifier;
  private AppLogger sLogger = AppLoggerFactory.create();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
          .detectDiskWrites()
          .detectNetwork()   // or .detectAll() for all detectable problems
          .penaltyLog()
          .build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
          .detectLeakedClosableObjects()
          .penaltyLog()
          .build());
    }

    super.onCreate(savedInstanceState);

    mApp = (GGApplication) getApplicationContext();
    LauncherActivityComponent launcherActivityComponent = DaggerLauncherActivityComponent.builder()
        .applicationComponent(mApp.getApplicationComponent())
        .build();

    launcherActivityComponent.inject(this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ensurePermissionsGrantedThenContinue();
    } else {
      continueWithPermissionsGranted();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      String[] permissions,
      int[] grantResults) {
    switch (requestCode) {
      case PERMISSIONS_REQUEST_CODE_MULTIPLE: {
        if (isAllGranted(grantResults)) {
          continueWithPermissionsGranted();
        } else {
          finish();
        }
      }
      break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  private void ensurePermissionsGrantedThenContinue() {
    final List<String> list = getNotGrantedPermissionsList();
    if (list.isEmpty()) {
      continueWithPermissionsGranted();
    } else {
      String[] notGrantedPermissions = list.toArray(new String[list.size()]);
      requestPermissions(notGrantedPermissions, PERMISSIONS_REQUEST_CODE_MULTIPLE);
    }
  }

  private List<String> getNotGrantedPermissionsList() {
    final List<String> list = new ArrayList<>();
    for (String permission : NEEDED_PERMISSIONS) {
      if (!isGranted(permission)) {
        list.add(permission);
      }
    }
    return list;
  }

  private boolean isGranted(String permission) {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
  }

  private void continueWithPermissionsGranted() {
    requestGpsLocationUpdates();
    startBackgroundTasks();
    startMainActivity();
  }

  private void requestGpsLocationUpdates() {
    if (!mLocationFetcher.isRequestingUpdates()) {
      tryToExecuteLocationUpdatesInteractor();
    }
  }

  private void startBackgroundTasks() {
    ApplicationComponent component = mApp.getApplicationComponent();

    component.updateUnreadCountInteractor().execute();
    component.displayUserLocationsInteractor().execute();
    component.loadAllCachedLayersInteractor().execute();
    component.processVectorLayersInteractor().execute();
    component.loadIntermediateRastersInteractor().execute();
    component.update3GConnectivityStatusInteractor().execute();

    mNotifyOnNewMessageInteractorFactory.create(mAppNotifier).execute();
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

  private boolean isAllGranted(int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }
}