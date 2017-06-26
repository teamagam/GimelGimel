package com.teamagam.gimelgimel.app.common.utils;

public class Constants {
  public static final float LOCATE_ME_BUTTON_VIEWER_SCALE = 10 * 1000;
  public static final int LOCATION_MIN_UPDATE_FREQUENCY_MS = 20 * 1000;
  public static final int LOCATION_RAPID_UPDATE_FREQUENCY_MS = 1000;
  public static final int LOCATION_THRESHOLD_UPDATE_DISTANCE_M = 3;

  public static final int UI_REFRESH_RATE_MS = 16;

  public static final String ARC_GIS_TILED_MAP_SERVICE_URL =
      "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer";
  public static final String OFFLINE_TPK_DIR_NAME = "tpks";
  public static final String OFFLINE_TPK_FILENAME = "israel.tpk";

  public static final double ISRAEL_WEST_LONG_ENVELOPE = 33;
  public static final double ISRAEL_SOUTH_LAT_ENVELOPE = 29;
  public static final double ISRAEL_EAST_LONG_ENVELOPE = 36;
  public static final double ISRAEL_NORTH_LAT_ENVELOPE = 34;

  public static final int VIEWER_ENTITY_CLICKING_TOLERANCE_DP = 20;
  public static final int VIEWER_MIN_SCALE_RATIO = 2000;
  public static final int VIEWER_LOOK_AT_ENVELOPE_PADDING_DP = 100;

  public static final double VIEWER_LOOK_AT_POINT_SCALE = 3500;

  public static final long GPS_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;

  public static final long DATA_STATUS_CONSISTENT_TIMEFRAME_MS = 5 * 1000;

  public static final int MESSAGES_NOTIFICATION_ID = 1;
  public static final int ALERTS_NOTIFICATION_ID = 2;

  // Log4jDiskLogger
  public static final String LOG_FILE_NAME_SUFFIX = "log.txt";
  public static final String LOG_DIR_NAME = "Logs";
  public static final int MAX_LOG_SIZE = 1024 * 1024 * 5;
  public static final int MAX_BACKUP_LOG_FILES = 10;

  public static final String DATABASE_NAME = "messages-cache";
}