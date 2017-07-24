package com.teamagam.gimelgimel.data.dynamicLayers.remote;

import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.DynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class DataDynamicLayerRemoteSourceHandler implements DynamicLayerRemoteSourceHandler {

  private final DynamicLayersAPI mDynamicLayersAPI;
  private final GeoEntityDataMapper mGeoEntityDataMapper;

  @Inject
  public DataDynamicLayerRemoteSourceHandler(DynamicLayersAPI dynamicLayersAPI,
      GeoEntityDataMapper geoEntityDataMapper) {
    mDynamicLayersAPI = dynamicLayersAPI;
    mGeoEntityDataMapper = geoEntityDataMapper;
  }

  @Override
  public void addEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity) {
    try {
      Response<Void> execute = mDynamicLayersAPI.addEntity(dynamicLayer.getId(),
          mGeoEntityDataMapper.transform(geoEntity)).execute();

      if (!execute.isSuccessful()) {
        throw new RuntimeException(
            "Couldn't add entity to server. error-body " + execute.errorBody().toString());
      }
    } catch (IOException e) {
      throw new RuntimeException("Couldn't add entity to server", e);
    }
  }
}
