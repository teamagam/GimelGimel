package com.teamagam.gimelgimel.app.common.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayUtils {

  public static int spToPx(float sp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getDisplayMetrics());
  }

  public static int pxToDp(int px) {
    return (int) (px / getDisplayDensity());
  }

  public static int dpToPx(int dp) {
    return (int) (dp * getDisplayDensity());
  }

  private static DisplayMetrics getDisplayMetrics() {
    return Resources.getSystem().getDisplayMetrics();
  }

  private static float getDisplayDensity() {
    return getDisplayMetrics().density;
  }
}
