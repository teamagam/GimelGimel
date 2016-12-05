package com.teamagam.gimelgimel.data.config;

import android.graphics.Bitmap;

public class Constants {
    public static final String MESSAGING_SERVER_URL = "http://ggmessaging-dev1.herokuapp.com";
    public static final long CONNECTION_SERVER_TIME_OUT_SECONDS = 120;

    public static final String IMAGE_MIME_TYPE = "image/jpeg";
    public static final String IMAGE_KEY = "image";

    public static final Bitmap.CompressFormat IMAGE_COMPRESS_TYPE = Bitmap.CompressFormat.JPEG;
    public static final int COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS = 1024;
    public static final int COMPRESSED_IMAGE_JPEG_QUALITY = 70;

    //Message Long polling exponential backoff configuration
    public static final int POLLING_EXP_BACKOFF_BASE_INTERVAL_MILLIS = 50;
    public static final int POLLING_EXP_BACKOFF_MULTIPLIER = 2;
    public static final int POLLING_EXP_BACKOFF_MAX_BACKOFF_MILLIS = 5 * 1000;

    public static final float MAXIMUM_GPS_SAMPLE_DEVIATION_METERS = 25;

    public static final String RECEIVED_MESSAGES_GEO_ENTITIES_LAYER_TAG = "ReceivedMessages";
    public static final String SENSOR_LAYER_TAG = "SensorLayerTag";

    public static final String LATEST_MESSAGE_DATE_KEY = "pref_date_ms";
}
