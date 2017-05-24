package com.teamagam.gimelgimel.data.images;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.config.Constants;
import id.zelory.compressor.Compressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;

public class ImageUtils {

  private static final String IMAGE_TEMP_PREFIX = "image_";
  private static final String IMAGE_TEMP_TYPE = ".png";
  private static final String TEMP_DIR = "images";
  private static final String DATE_FORMAT = "dd_HH_mm_ss";

  @Inject
  Context mContext;

  @Inject
  ExternalDirProvider mExternalDirProvider;

  @Inject
  public ImageUtils() {
  }

  public Uri getTempImageUri() {
    File externalCacheDir = mExternalDirProvider.getExternalCacheDir();
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
    File compressorImage =
        new Compressor.Builder(mContext).setCompressFormat(Constants.IMAGE_COMPRESS_TYPE)
            .setMaxHeight(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS)
            .setMaxWidth(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS)
            .setQuality(Constants.COMPRESSED_IMAGE_JPEG_QUALITY)
            .build()
            .compressToFile(image);

    return getImageBytes(compressorImage);
  }

  public String convertImageToBase64(File image) {
    byte[] imageBytes = readAndCompressImage(image);

    return convertImageToBase64(imageBytes);
  }

  public String convertImageToBase64(byte[] imageBytes) {
    return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
  }

  private byte[] getImageBytes(File image) {
    try (FileInputStream stream = new FileInputStream(image.getAbsolutePath())) {
      byte[] compressedImageBytes = new byte[(int) image.length()];

      stream.read(compressedImageBytes);
      stream.close();

      return compressedImageBytes;
    } catch (IOException ex) {
      throw new RuntimeException("Could not read image", ex);
    }
  }
}
