package com.teamagam.gimelgimel.data.dynamicLayers.remote;

import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DynamicLayersAPI {

  @POST("dynamicLayers/{layer_id}/")
  Call<Void> addEntity(@Path("layer_id") String layerId, @Body GeoContentData geoContentData);

  @DELETE("dynamicLayers/{layer_id}/{entity_id}")
  Call<Void> removeEntity(@Path("layer_id") String layerId, @Path("entity_id") String entityId);
}

