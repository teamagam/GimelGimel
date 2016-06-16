package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.teamagam.gimelgimel.app.common.logging.LogWrapper;
import com.teamagam.gimelgimel.app.common.logging.LogWrapperFactory;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/22/2016.
 * TODO: complete text
 */
public class GGImageService extends IntentService {

    //todo: config
    private final int IMAGE_MAX_SIZE = 1024;
    private final int IMAGE_JPEG_QUALITY = 70;
    private final Bitmap.CompressFormat IMAGE_COMPRESS_TYPE = Bitmap.CompressFormat.JPEG;

    private static final String IMAGE_KEY = "image";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";
    private static final LogWrapper LOGGER = LogWrapperFactory.create(GGImageService.class);

    public GGImageService() {
        super(GGImageService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Uri imageUri = intent.getData();
        long time = (long) extras.get(IImageSender.TIME);
        PointGeometry loc = (PointGeometry) extras.get(IImageSender.LOCATION);

        File imageFile = new File(imageUri.getPath());
        File compressedFile = new File(imageFile.getParentFile(),
                "compressed_" + imageFile.getName());
        boolean success = compressImage(imageFile, compressedFile);

        if (success) {
            sendImage(compressedFile, loc, time);
            LOGGER.v("Image successfully compressed to: " + compressedFile.getPath());
        } else {
            LOGGER.w("Unsuccessful Image compressing");
            Toast.makeText(getApplicationContext(), "Unsuccessful Image Upload",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //this method is here so it won't be static in utils.
    private boolean compressImage(File sourceFile, File targetFile) {

        //Decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(sourceFile.getPath(), options);

        int scale = 1;
        if (options.outHeight > IMAGE_MAX_SIZE || options.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        Bitmap b = BitmapFactory.decodeFile(sourceFile.getPath(), options);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            b.compress(IMAGE_COMPRESS_TYPE, IMAGE_JPEG_QUALITY, fos);
            fos.close();
        } catch (IOException e) {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                LOGGER.e("Unsuccessful Image compressing: ", e1);
                return false;
            }
            LOGGER.e("Unsuccessful Image compressing: ", e);
            return false;
        }

        return true;
    }


    private void sendImage(File imageFile, final PointGeometry loc, final long imageTime) {
        ImageMetadata meta = new ImageMetadata(imageTime, loc, ImageMetadata.USER);
        String senderId = NetworkUtil.getMac();
        Message msg = new MessageImage(senderId, meta);

        GGFileUploader.uploadFile(imageFile, IMAGE_KEY, IMAGE_MIME_TYPE, msg,
                new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call,
                                           Response<Message> response) {
                        if (!response.isSuccessful()) {
                            LOGGER.w("Unsuccessful Image Upload: " + response.errorBody());
                            Toast.makeText(getApplicationContext(), "Unsuccessful Image Upload",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MessageImage msg = (MessageImage) response.body();
                        LOGGER.d("Upload succeeded to: " + msg.getContent().getURL());
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        LOGGER.d("FAIL in uploading image to the server", t);
                    }
                });
    }
}
