package com.teamagam.gimelgimel.app.network.services;

import android.net.Uri;
import android.util.Log;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.network.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;
import com.teamagam.gimelgimel.app.utils.NetworkUtil;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

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
    public void sendImage(Uri imageUri, final PointGeometry loc, final long imageTime) {
        File file = new File(imageUri.getPath());
        ImageMetadata meta = new ImageMetadata(imageTime, loc, ImageMetadata.USER);
        GGMessagingUtils.sendImageMessageAsync(meta);
        String senderId = NetworkUtil.getMac();
        Message msg = new MessageImage(senderId, meta);

        uploadFile(file, IMAGE_KEY, msg, new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call,
                                   Response<Message> response) {
                if (!response.isSuccessful()) {
                    Log.w(LOG_TAG, "Unsuccessful Image Upload: " + response.errorBody());
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

    private void uploadFile(File file, String file_key, Message msg, Callback<Message> callback) {

        // create upload service client
//        GGStorageAPI service = RestStorageAPI.getInstance().getStorageAPI();
        GGMessagingAPI service = RestAPI.getInstance().getMessagingAPI();

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(file_key, file.getName(), requestFile);

        // add another part within the multipart request
//        String descriptionString = "";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<Message> call = service.sendImage(msg, body);
        call.enqueue(callback);
    }
}
