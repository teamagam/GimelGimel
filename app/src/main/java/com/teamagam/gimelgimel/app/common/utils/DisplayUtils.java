package com.teamagam.gimelgimel.app.common.utils;

import android.content.res.Resources;

public class DisplayUtils {

  public static int pxToDp(int px) {
    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
  }

  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }
}
