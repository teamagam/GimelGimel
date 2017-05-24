package com.teamagam.gimelgimel.domain.utils;

import com.teamagam.gimelgimel.domain.config.Constants;

public class TextUtils {

  public static boolean isOnlyWhiteSpaces(String str) {
    return str.trim().length() <= 0;
  }

  public static boolean isValidDisplayName(String username) {
    return !isOnlyWhiteSpaces(username) && username.length() <= Constants.DISPLAY_NAME_MAX_LENGTH;
  }
}
