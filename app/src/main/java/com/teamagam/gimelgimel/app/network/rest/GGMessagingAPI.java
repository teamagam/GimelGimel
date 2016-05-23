package com.teamagam.gimelgimel.app.network.rest;


import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;

/***
 * An interface to describe the GG REST full API
 */
public interface GGMessagingAPI {

    @GET("/long/messages/fromDate/{fromDate}")
    Call<List<Message>> getMessagesFromDate(@Path("fromDate") long fromDateMs);

    @GET("/long/messages")
    Call<List<Message>> getMessages();

    @POST("/messages/")
    Call<Message> postMessage(@Body Message message);
}
