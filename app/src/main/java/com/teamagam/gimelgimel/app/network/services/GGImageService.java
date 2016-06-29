package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pre-processes and uploads (to GGMessaging) images
 * Pre-processing includes attaching meta-data to image and compressing image file
 */
public class GGImageService extends IntentService {

    private final Bitmap.CompressFormat IMAGE_COMPRESS_TYPE = Bitmap.CompressFormat.JPEG;

    private static final String LOG_TAG = GGImageService.class.getSimpleName();
    private static final String IMAGE_KEY = "image";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";

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
            Log.v(LOG_TAG, "Image successfully compressed to: " + compressedFile.getPath());
        } else {
            Log.w(LOG_TAG, "Unsuccessful Image compressing: ");
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
        if (options.outHeight > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS || options.outWidth > Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS) {
            scale = (int) Math.pow(2,
                    (int) Math.ceil(Math.log(Constants.COMPRESSED_IMAGE_MAX_DIMENSION_PIXELS /
                            (double) Math.max(options.outHeight, options.outWidth)) / Math.log(
                            0.5)));
        }

        //Decode with inSampleSize
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        Bitmap b = BitmapFactory.decodeFile(sourceFile.getPath(), options);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            b.compress(IMAGE_COMPRESS_TYPE, Constants.COMPRESSED_IMAGE_JPEG_QUALITY, fos);
            fos.close();
        } catch (IOException e) {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(LOG_TAG, "Unsuccessful Image compressing: ", e1);
                return false;
            }
            Log.e(LOG_TAG, "Unsuccessful Image compressing: ", e);
            return false;
        }

        return true;
    }


    private void sendImage(File imageFile, final PointGeometry loc, final long imageTime) {
        ImageMetadata meta = new ImageMetadata(imageTime, loc, ImageMetadata.USER);
        String senderId = GGMessageSender.getUserName(this);
        Message msg = new MessageImage(senderId, meta);

        GGFileUploader.uploadFile(imageFile, IMAGE_KEY, IMAGE_MIME_TYPE, msg,
                new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call,
                                           Response<Message> response) {
                        if (!response.isSuccessful()) {
                            Log.w(LOG_TAG, "Unsuccessful Image Upload: " + response.errorBody());
                            Toast.makeText(getApplicationContext(), "Unsuccessful Image Upload",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MessageImage msg = (MessageImage) response.body();
                        Log.d(LOG_TAG, "Upload succeeded to: " + msg.getContent().getURL());
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.d(LOG_TAG, "FAIL in uploading image to the server", t);
                    }
                });
    }
}
