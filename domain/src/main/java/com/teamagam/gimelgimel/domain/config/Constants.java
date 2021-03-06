package com.teamagam.gimelgimel.domain.config;

public class Constants {
  public static final String USERNAME_PREFERENCE_KEY = "user_name_text";
  public static final String DEFAULT_USERNAME = "New Name";
  public static final String USE_UTM_PREF_KEY = "pref_use_utm";
  public static final String NOTIFICATION_ALERT_ONLY_PREF_KEY = "alerts_only_key";

  public static final long USER_LOCATION_STALE_THRESHOLD_MS = 5 * 60 * 1000;
  public static final long USER_LOCATION_RELEVANCE_THRESHOLD_MS = 48 * 60 * 60 * 1000;

  public static final long USERS_LOCATION_REFRESH_FREQUENCY_MS = 5 * 1000;

  public static final int DISPLAY_NAME_MAX_LENGTH = 20;

  public static final int RETRIES = 3;
  public static final long RETRIES_DELAY_MS = 2 * 1000;

  public static final int LOCATION_DISTANCE_CHANGE_SERVER_UPDATE_THRESHOLD_METERS = 20;

  public static final int LOCATION_TIME_CHANGE_SERVER_UPDATE_THRESHOLD_MS = 60 * 1000;
  public static final Object SIGNAL = new Object();
}
