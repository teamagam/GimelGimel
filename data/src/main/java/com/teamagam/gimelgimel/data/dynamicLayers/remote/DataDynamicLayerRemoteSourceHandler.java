package com.teamagam.gimelgimel.data.dynamicLayers.remote;

import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.DynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

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
  public Observable<Void> addEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity) {
    return mDynamicLayersAPI.addEntity(dynamicLayer.getId(),
        mGeoEntityDataMapper.transform(geoEntity));
  }
}
