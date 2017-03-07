package com.teamagam.gimelgimel.data.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.config.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {

    private static final String IMAGE_TEMP_PREFIX = "image_";
    private static final String IMAGE_TEMP_TYPE = ".png";
    private static final String TEMP_DIR = "images";
    private static final String DATE_FORMAT = "dd_HH_mm_ss";

    public static Uri getTempImageUri(ExternalDirProvider externalDirProvider){
        File externalCacheDir = externalDirProvider.getExternalCacheDir();
        File tempDir = new File(externalCacheDir, TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String name = IMAGE_TEMP_PREFIX + timeStamp + IMAGE_TEMP_TYPE;

        File tempFile = new File(tempDir, name);

        return Uri.fromFile(tempFile);
    }

    public static byte[] readAndCompressImage(File image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), options);

        int scale = getScaleSize(options.outHeight, options.outWidth);

        //Decode with inSampleSize
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap compressedBitmap = BitmapFactory.decodeFile(image.getPath(), options);

        compressedBitmap.compress(Constants.IMAGE_COMPRESS_TYPE, Constants.COMPRESSED_IMAGE_JPEG_QUALITY, stream);

        return stream.toByteArray();
    }

    private static int getScaleSize(int outHeight, int outWidth) {
        int scale = 1;

        if (outHeight > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS ||
                outWidth > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS) {
            scale = (int) Math.pow(2,
                    (int) Math.ceil(Math.log(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS /
                            (double) Math.max(outHeight, outWidth)) / Math.log(0.5)
                    ));
        }

        return scale;
    }
}
