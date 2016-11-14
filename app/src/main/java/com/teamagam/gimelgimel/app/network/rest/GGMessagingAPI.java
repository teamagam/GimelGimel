package com.teamagam.gimelgimel.app.network.rest;


import com.teamagam.gimelgimel.app.message.model.MessageApp;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/***
 * An interface to describe the GG REST full API
 */
public interface GGMessagingAPI {

    @GET("/long/messages/fromDate/{fromDate}")
    Call<List<MessageApp>> getMessagesFromDate(@Path("fromDate") long fromDateMs);

    @GET("/long/messages")
    Call<List<MessageApp>> getMessages();

    @POST("/messages/")
    Call<MessageApp> postMessage(@Body MessageApp message);

    @Multipart
    @POST("/images")
    Call<MessageApp> sendImage(@Part("message") MessageApp message,
                               @Part MultipartBody.Part file);
}
