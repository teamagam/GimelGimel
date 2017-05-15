package com.teamagam.gimelgimel.domain.config;

public class Constants {
    public static final String USERNAME_PREFERENCE_KEY = "user_name_text";

    public static final long USER_LOCATION_STALE_THRESHOLD_MS = 60 * 1000;
    public static final long USER_LOCATION_RELEVANCE_THRESHOLD_MS = 12 * 60 * 60 * 1000;

    public static final long USERS_LOCATION_REFRESH_FREQUENCY_MS = 5 * 1000;

    public static final int LAYER_CACHING_RETRIES = 3;
    public static final long LAYER_CACHING_RETRIES_DELAY_MS = 2 * 1000;
    public static final int LOCATION_CHANGE_METERS_SERVER_UPDATE_THRESHOLD = 20;
    public static final int LOCATION_TIME_CHANGE_SERVER_UPDATE_THRESHOLD = 20 * 1000;
}
