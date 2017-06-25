package com.teamagam.gimelgimel.app.common.utils;

import android.app.ActivityManager;
import android.content.Context;

public class Environment {
  private Context mContext;
  private ActivityManager mActivityManager;

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
