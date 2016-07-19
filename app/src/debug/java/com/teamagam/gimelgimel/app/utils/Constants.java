package com.teamagam.gimelgimel.app.utils;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final String MESSAGING_SERVER_URL = "http://ggmessaging-dev2.herokuapp.com";

    public static final String APPLICATION_NAME = "GimelGimel";

    public static final int COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS = 1024;
    public static final int COMPRESSED_IMAGE_JPEG_QUALITY = 70;

    public static final float MAP_VIEW_INITIAL_BOUNDING_BOX_EAST = 36.0f;
    public static final float MAP_VIEW_INITIAL_BOUNDING_BOX_WEST = 34.0f;
    public static final float MAP_VIEW_INITIAL_BOUNDING_BOX_NORTH = 34.0f;
    public static final float MAP_VIEW_INITIAL_BOUNDING_BOX_SOUTH = 29.0f;

    public static final float MAXIMUM_GPS_SAMPLE_DEVIATION_METERS = 50;

    public static final float LOCATE_ME_BUTTON_ALTITUDE_METERS = 500;

    public static final String CESIUM_HTML_LOCAL_FILEPATH = "file:///android_asset/cesiumHelloWorld.html";

    public static final long GPS_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;

    public static final long USER_LOCATION_STALE_THRESHOLD_MS = 60 * 1000;
    public static final int USER_LOCATION_PIN_SIZE_PX = 48;
    public static final String ACTIVE_USER_LOCATION_PIN_CSS_COLOR = "#7FFF00";
    public static final String STALE_USER_LOCATION_PIN_CSS_COLOR = "#A9A9A9";

    public static final long USERS_LOCATION_REFRESH_FREQUENCY_MS = 5 * 1000;
    public static final float ZOOM_IN_FACTOR = 0.5f;

    //Disk-Logger
    public static final String LOG_FILE_NAME_SUFFIX = "log.txt";
    public static final String LOG_DIR_NAME = "Logs";
    public static final int MAX_WRITE_RETRIES = 3;

    //Message Long polling exponential backoff configuration
    public static final int POLLING_EXP_BACKOFF_BASE_INTERVAL_MILLIS = 50;
    public static final int POLLING_EXP_BACKOFF_MULTIPLIER = 2;
    public static final int POLLING_EXP_BACKOFF_MAX_BACKOFF_MILLIS = 60 * 1000;

    // Log4jDiskLogger
    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory()
            + File.separator + APPLICATION_NAME + File.separator + LOG_DIR_NAME;
    public static final int MAX_LOG_SIZE = 1024 * 1024 * 5;
    public static final int MAX_BACKUP_LOG_FILES = 10;
}