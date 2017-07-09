package com.teamagam.gimelgimel.data.response.rest;

import com.teamagam.gimelgimel.data.response.entity.ServerIconResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IconsAPI {
  @GET("/icons")
  Call<List<ServerIconResponse>> getServerIconsCall();
}
