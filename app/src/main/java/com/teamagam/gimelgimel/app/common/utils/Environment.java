package com.teamagam.gimelgimel.app.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import com.teamagam.gimelgimel.domain.utils.ApplicationStatus;
import javax.inject.Inject;

public class Environment implements ApplicationStatus {
  private Context mContext;
  private ActivityManager mActivityManager;

  @Inject
  public Environment(Context context) {
    mContext = context;
    mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
  }

  public boolean isAppOnForeground() {
    ActivityManager.RunningTaskInfo foregroundTaskInfo = mActivityManager.getRunningTasks(1).get(0);
    String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
    String packageName = mContext.getPackageName();

    return packageName.equals(foregroundTaskPackageName);
  }
}
