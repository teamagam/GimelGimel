package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/22/2016.
 * TODO: complete text
 */
public class GGImageService extends IntentService{

    private static final String LOG_TAG = GGImageService.class.getSimpleName();
    private static final String IMAGE_KEY = "image";
    private static final String IMAGE_MIME_TYPE = "image/*";

    public GGImageService() {
        super(GGImageService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Uri imageUri = intent.getData();
        long time = (long) extras.get(IImageSender.TIME);
        PointGeometry loc = (PointGeometry) extras.get(IImageSender.LOCATION);

        Uri compressedImageUri = compressImage(imageUri);

        sendImage(compressedImageUri, loc, time);
    }


    private Uri compressImage(Uri imageUri){
        return imageUri;
    }


    private void sendImage(Uri imageUri, final PointGeometry loc, final long imageTime) {
        File file = new File(imageUri.getPath());
        ImageMetadata meta = new ImageMetadata(imageTime, loc, ImageMetadata.USER);
        String senderId = NetworkUtil.getMac();
        Message msg = new MessageImage(senderId, meta);

        GGFileUploader.uploadFile(file, IMAGE_KEY, IMAGE_MIME_TYPE, msg, new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call,
                                   Response<Message> response) {
                if (!response.isSuccessful()) {
                    Log.w(LOG_TAG, "Unsuccessful Image Upload: " + response.errorBody());
                    Toast.makeText(getApplicationContext(), "Unsuccessful Image Upload", Toast.LENGTH_SHORT).show();
                    return;
                }

                MessageImage msg = (MessageImage) response.body();
                Log.d(LOG_TAG, "Upload succeeded to: " + msg.getContent().getURL());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }
}
