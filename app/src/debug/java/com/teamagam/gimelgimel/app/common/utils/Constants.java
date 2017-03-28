package com.teamagam.gimelgimel.app.common.utils;


public class Constants {
    public static final float LOCATE_ME_BUTTON_VIEWER_SCALE = 10 * 1000;
    public static final int COMPASS_REFRESH_RATE_MS = 16;

    public static final String ARC_GIS_TILED_MAP_SERVICE_URL = "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer";
    public static final String OFFLINE_TPK_DIR_NAME = "tpks";
    public static final String OFFLINE_TPK_FILENAME = "israel.tpk";

    public static final double ISRAEL_WEST_LONG_ENVELOPE = 33;
    public static final double ISRAEL_SOUTH_LAT_ENVELOPE = 29;
    public static final double ISRAEL_EAST_LONG_ENVELOPE = 36;
    public static final double ISRAEL_NORTH_LAT_ENVELOPE = 34;

    public static final int VIEWER_ENTITY_CLICKING_TOLERANCE_DP = 20;
    public static final int VIEWER_MIN_SCALE_RATIO = 2000;

    public static final long GPS_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;

    public static final long DATA_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;
    public static final int USER_LOCATION_PIN_SIZE_PX = 48;
    public static final String ACTIVE_USER_LOCATION_PIN_CSS_COLOR = "#7FFF00";

    public static final String STALE_USER_LOCATION_PIN_CSS_COLOR = "#A9A9A9";

    public static final float ZOOM_IN_FACTOR = 0.5f;

    public static final int DISPLAY_NAME_MAX_LENGTH = 20;
    // Log4jDiskLogger
    public static final String LOG_FILE_NAME_SUFFIX = "log.txt";
    public static final String LOG_DIR_NAME = "Logs";
    public static final int MAX_LOG_SIZE = 1024 * 1024 * 5;
    public static final int MAX_BACKUP_LOG_FILES = 10;
}