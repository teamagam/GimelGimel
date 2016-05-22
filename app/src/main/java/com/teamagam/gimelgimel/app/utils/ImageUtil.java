package com.teamagam.gimelgimel.app.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 5/22/2016.
 * Util for Images
 */
public class ImageUtil {

    private static final String IMAGE_TEMP_PREFIX = "image_";
    private static final String IMAGE_TEMP_TYPE = ".png";
    private static final String TEMP_DIR = "images";
    private static final String DATE_FORMAT = "dd_HH_mm_ss";

    /**
     * needed for temp file from camera.
     * @param context
     * @return
     * @throws IOException
     */
    public static Uri getTempImageUri(Context context) throws IOException {

        //temp dir
        File tempDir = new File(context.getExternalCacheDir(), TEMP_DIR);
        if(!tempDir.exists()) {
            tempDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String name = IMAGE_TEMP_PREFIX + timeStamp;

        File tempFile = File.createTempFile(name, IMAGE_TEMP_TYPE, tempDir);
        tempFile.delete();

        return Uri.fromFile(tempFile);
    }
}
