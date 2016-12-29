package com.teamagam.gimelgimel.app.common.utils;


public class Constants {
    public static final float LOCATE_ME_BUTTON_ALTITUDE_METERS = 500;

    public static final String CESIUM_HTML_LOCAL_FILEPATH = "file:///android_asset/cesiumHelloWorld.html";

    public static final long GPS_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;
    public static final long DATA_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;

    public static final int USER_LOCATION_PIN_SIZE_PX = 48;
    public static final String ACTIVE_USER_LOCATION_PIN_CSS_COLOR = "#7FFF00";
    public static final String STALE_USER_LOCATION_PIN_CSS_COLOR = "#A9A9A9";

    public static final float ZOOM_IN_FACTOR = 0.5f;

    // Log4jDiskLogger
    public static final String LOG_FILE_NAME_SUFFIX = "log.txt";
    public static final String LOG_DIR_NAME = "Logs";
    public static final int MAX_LOG_SIZE = 1024 * 1024 * 5;
    public static final int MAX_BACKUP_LOG_FILES = 10;
}