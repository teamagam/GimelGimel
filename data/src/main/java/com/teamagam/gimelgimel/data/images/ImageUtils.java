package com.teamagam.gimelgimel.data.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.teamagam.gimelgimel.data.config.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageUtils {

    public static byte[] compressImage(File image) {
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
