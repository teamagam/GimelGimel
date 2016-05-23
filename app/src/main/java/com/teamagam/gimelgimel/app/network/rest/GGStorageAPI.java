package com.teamagam.gimelgimel.app.network.rest;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/***
 * An interface to describe the GG REST full API
 */
public interface GGStorageAPI {

//    @GET("/{id}")
//    Call<File> getMessagesFromDate(@Path("id") long id);

    @Multipart
    @POST("/uploads")
    Call<String> uploadFile(@Part("image") RequestBody description,
                         @Part MultipartBody.Part file);
}
