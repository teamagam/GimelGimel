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
 * uses Rest API to upload single file to the server
 */
public class GGFileUploader {

    /**
     * async file uploader
     * @param file - file to upload
     * @param file_key - key in the form-data http request
     * @param mimeType - type of the file (i.e. image/jpeg)
     * @param msg - msg to send (the key will be "message")
     * @param callback - retrofit callback for success and failure.
     */
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
