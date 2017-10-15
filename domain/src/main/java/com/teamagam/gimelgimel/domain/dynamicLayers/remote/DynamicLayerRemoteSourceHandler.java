package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

public interface DynamicLayerRemoteSourceHandler {

  void createDynamicLayer(String name, String description);

  void addEntity(DynamicLayer dynamicLayer, DynamicEntity dynamicEntity);

  void removeEntity(DynamicLayer dynamicLayer, DynamicEntity dynamicEntity);

  void updateDescription(DynamicLayer dynamicLayer);
}
