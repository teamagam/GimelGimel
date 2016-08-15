package com.teamagam.gimelgimel.data.message.rest;


import com.teamagam.gimelgimel.domain.messages.entities.Message;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/***
 * An interface to describe the GG REST full API
 */
public interface GGMessagingAPI {

    @GET("/long/messages/fromDate/{fromDate}")
    Observable<List<Message>> getMessagesFromDate(@Path("fromDate") long fromDateMs);

    @GET("/long/messages")
    Observable<List<Message>> getMessages();

    @POST("/messages/")
    Observable<Message> postMessage(@Body Message messageData);

    @Multipart
    @POST("/images")
    Observable<Message> sendImage(@Part("message") Message messageData,
                                  @Part MultipartBody.Part file);
}
