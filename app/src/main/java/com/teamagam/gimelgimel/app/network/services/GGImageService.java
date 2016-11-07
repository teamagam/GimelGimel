package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.utils.Constants;

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

    private static final String IMAGE_KEY = "image";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";
    private static final Logger sLogger = LoggerFactory.create(GGImageService.class);

    public GGImageService() {
        super(GGImageService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Uri imageUri = intent.getData();
        long time = (long) extras.get(IImageSender.TIME);
        PointGeometryApp loc = (PointGeometryApp) extras.get(IImageSender.LOCATION);

        File imageFile = new File(imageUri.getPath());
        File compressedFile = new File(imageFile.getParentFile(),
                "compressed_" + imageFile.getName());
        boolean success = compressImage(imageFile, compressedFile);

        if (success) {
            sendImage(compressedFile, loc, time);
            sLogger.v("Image successfully compressed to: " + compressedFile.getPath());
        } else {
            sLogger.w("Unsuccessful Image compressing");
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
                sLogger.e("Unsuccessful Image compressing: ", e1);
                return false;
            }
            sLogger.e("Unsuccessful Image compressing: ", e);
            return false;
        }

        return true;
    }


    private void sendImage(File imageFile, final PointGeometryApp loc, final long imageTime) {
//        ImageMetadataApp meta = new ImageMetadataApp(imageTime, loc, ImageMetadataApp.USER);
        ImageMetadataApp meta = null;
        String senderId = GGMessageSender.getUserName(this);
        MessageApp msg = new MessageImageApp(meta);

        GGFileUploader.uploadFile(imageFile, IMAGE_KEY, IMAGE_MIME_TYPE, msg,
                new Callback<MessageApp>() {
                    @Override
                    public void onResponse(Call<MessageApp> call,
                                           Response<MessageApp> response) {
                        if (!response.isSuccessful()) {
                            sLogger.w("Unsuccessful Image Upload: " + response.errorBody());
                            Toast.makeText(getApplicationContext(), "Unsuccessful Image Upload",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MessageImageApp msg = (MessageImageApp) response.body();

                        // Send the current status of the network
                        ConnectivityStatusReceiver.broadcastAvailableNetwork(GGImageService.this);

                        sLogger.d("Upload succeeded to: " + msg.getContent().getURL());
                    }

                    @Override
                    public void onFailure(Call<MessageApp> call, Throwable t) {
                        // Send the current status of the network
                        ConnectivityStatusReceiver.broadcastNoNetwork(GGImageService.this);

                        sLogger.d("FAIL in uploading image to the server", t);
                    }
                });
    }
}
