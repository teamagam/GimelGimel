package com.teamagam.gimelgimel.data.message.rest;


import com.teamagam.gimelgimel.data.message.entity.ConfirmMessageReadData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;

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
    Observable<List<MessageData>> getMessagesFromDate(@Path("fromDate") long fromDateMs);

    @GET("/long/messages")
    Observable<List<MessageData>> getMessages();

    @POST("/messages/")
    Observable<MessageData> postMessage(@Body MessageData messageData);

    @Multipart
    @POST("/messages/images")
    Observable<MessageData> sendImage(@Part("message") MessageData messageData,
                                  @Part MultipartBody.Part file);

    @POST("/messages/images/base64")
    Observable<MessageData> sendImage(@Body MessageData messageData);

    @POST("/messages/readMessages")
    void informReadMessage(@Body ConfirmMessageReadData confirmRead);
}
