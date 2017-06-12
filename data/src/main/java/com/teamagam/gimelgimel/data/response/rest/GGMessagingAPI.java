package com.teamagam.gimelgimel.data.response.rest;

import com.teamagam.gimelgimel.data.response.entity.ConfirmMessageReadResponse;
import com.teamagam.gimelgimel.data.response.entity.GGResponse;
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
  Observable<List<GGResponse>> getMessagesFromDate(@Path("fromDate") long fromDateMs);

  @GET("/long/messages")
  Observable<List<GGResponse>> getMessages();

  @POST("/messages/")
  Observable<GGResponse> postMessage(@Body GGResponse response);

  @Multipart
  @POST("/messages/images")
  Observable<GGResponse> sendImage(@Part("message") GGResponse response,
      @Part MultipartBody.Part file);

  @POST("/messages/images/base64")
  Observable<GGResponse> sendImage(@Body GGResponse response);

  @POST("/readMessages")
  Observable<ConfirmMessageReadResponse> informReadMessage(@Body
      ConfirmMessageReadResponse confirmRead);
}
