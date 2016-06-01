package com.teamagam.gimelgimel.app.network.services;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.network.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.app.network.rest.RestAPI;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created on 5/31/2016.
 * TODO: complete text
 */
public class GGFileUploader {

    public static void uploadFile(File file, String file_key, String mimeType, Message msg, Callback<Message> callback) {

        // create upload service client
        GGMessagingAPI service = RestAPI.getInstance().getMessagingAPI();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse(mimeType), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(file_key, file.getName(), requestFile);

        // finally, execute the request.
        // The message is translated to json with the regular adapter.
        Call<Message> call = service.sendImage(msg, body);
        call.enqueue(callback);
    }
}
