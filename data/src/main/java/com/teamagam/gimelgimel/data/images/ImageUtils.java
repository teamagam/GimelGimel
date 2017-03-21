package com.teamagam.gimelgimel.data.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.config.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import id.zelory.compressor.Compressor;

public class ImageUtils {

    private static final String IMAGE_TEMP_PREFIX = "image_";
    private static final String IMAGE_TEMP_TYPE = ".png";
    private static final String TEMP_DIR = "images";
    private static final String DATE_FORMAT = "dd_HH_mm_ss";

    @Inject
    Context mContext;

    @Inject
    public ImageUtils() {
    }

    public Uri getTempImageUri(ExternalDirProvider externalDirProvider) {
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

    public byte[] readAndCompressImage(File image) {
        File compressorImage = new Compressor.Builder(mContext)
                .setCompressFormat(Constants.IMAGE_COMPRESS_TYPE)
                .setMaxHeight(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS)
                .setMaxWidth(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS)
                .setQuality(Constants.COMPRESSED_IMAGE_JPEG_QUALITY)
                .build().compressToFile(image);

        return getImageBytes(compressorImage);
    }

    private byte[] getImageBytes(File image) {
        try {
            byte[] compressedImageBytes = new byte[(int) image.length()];
            FileInputStream stream = new FileInputStream(image.getAbsolutePath());
            stream.read(compressedImageBytes);
            stream.close();
            return compressedImageBytes;
        } catch (IOException ex) {
            throw new RuntimeException("Could not read image");
        }
    }
}
