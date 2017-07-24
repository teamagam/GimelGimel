package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public interface DynamicLayerRemoteSourceHandler {

  void addEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity);
}
