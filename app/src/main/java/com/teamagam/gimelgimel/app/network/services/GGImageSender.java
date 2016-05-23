package com.teamagam.gimelgimel.app.network.services;

import android.net.Uri;
import android.util.Log;

import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.network.rest.GGStorageAPI;
import com.teamagam.gimelgimel.app.network.rest.RestStorageAPI;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/22/2016.
 * TODO: complete text
 */
public class GGImageSender implements IImageSender {

    private static final String LOG_TAG = GGImageSender.class.getSimpleName();
    private static final String IMAGE_KEY = "image";

    @Override
    public void sendImage(Uri imageUri, final LocationSample imageLocation, final long imageTime) {
        File file = new File(imageUri.getPath());
        uploadFile(file, IMAGE_KEY, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                Log.v("Upload", "success");
                response.body();
                ImageMetadata meta = new ImageMetadata(imageTime, response, imageLocation, SourceType.User)
                GGMessagingUtils.sendImageMessageAsync(meta);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }

    private void uploadFile(File file, String file_key, Callback<String> callback) {

        // create upload service client
        GGStorageAPI service = RestStorageAPI.getInstance().getStorageAPI();

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(file_key, file.getName(), requestFile);

        // add another part within the multipart request
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<String> call = service.uploadFile(requestFile, body);
        call.enqueue(callback);
    }
}
