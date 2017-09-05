package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public interface DynamicLayerRemoteSourceHandler {

  void createDynamicLayer(String name, String description);

  void addEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity);

  void removeEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity);
}
