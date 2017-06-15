package com.teamagam.gimelgimel.data.response.rest;

import com.teamagam.gimelgimel.data.response.entity.ConfirmMessageReadResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import io.reactivex.Observable;
import java.util.List;
import okhttp3.MultipartBody;
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
  Observable<List<ServerResponse>> getMessagesFromDate(@Path("fromDate") long fromDateMs);

  @GET("/long/messages")
  Observable<List<ServerResponse>> getMessages();

  @POST("/messages/")
  Observable<ServerResponse> postMessage(@Body ServerResponse response);

  @Multipart
  @POST("/messages/images")
  Observable<ServerResponse> sendImage(@Part("message") ServerResponse response,
      @Part MultipartBody.Part file);

  @POST("/messages/images/base64")
  Observable<ServerResponse> sendImage(@Body ServerResponse response);

  @POST("/readMessages")
  Observable<ConfirmMessageReadResponse> informReadMessage(
      @Body ConfirmMessageReadResponse confirmRead);
}
