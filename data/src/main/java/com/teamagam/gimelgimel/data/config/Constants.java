package com.teamagam.gimelgimel.data.config;

import android.graphics.Bitmap;

public class Constants {
    public static final String MESSAGING_SERVER_URL = "http://ggmessaging-dev2.herokuapp.com";
    public static final long CONNECTION_SERVER_TIME_OUT_SECONDS = 120;

    public static final boolean USE_UTM = true;

    //Proxy
    public static final boolean SHOULD_USE_PROXY = false;
    public static final String PROXY_HOST = "proxyhost";
    public static final int PROXY_PORT = 443;
    public static final int DNS_ID = 2;

    public static final boolean USE_DOWNLOADS_DIR_AS_HOME = true;

    public static final boolean SHOULD_USE_BASE64_IMAGE = false;
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

    public static final String LATEST_MESSAGE_DATE_KEY = "pref_date_ms";
    public static final String COORDINATE_SYSTEM_PREF_KEY = "coordinate_system";

    public static final String VECTOR_LAYERS_CACHE_DIR_NAME = "VectorLayers";
    public static final String VECTOR_LAYER_CACHE_PREFIX = "layer";

    public static final String RASTERS_CACHE_DIR_NAME = "tpk";
}
