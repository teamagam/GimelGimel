package com.teamagam.gimelgimel.domain.dynamicLayers.details;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.HashMap;
import java.util.Map;

public class DynamicLayerToEntityMapper {

  private final Map<DynamicEntity, DynamicLayer> mEntityToLayer;
  private final Map<String, DynamicLayer> mGeoEntityToLayer;

  public DynamicLayerToEntityMapper() {
    mEntityToLayer = new HashMap<>();
    mGeoEntityToLayer = new HashMap<>();
  }

  public void map(DynamicLayer layer) {
    for (DynamicEntity entity : layer.getEntities()) {
      map(layer, entity);
    }
  }

  public DynamicLayer get(DynamicEntity entity) {
    return mEntityToLayer.get(entity);
  }

  public DynamicLayer getByGeoEntity(String geoEntityId) {
    return mGeoEntityToLayer.get(geoEntityId);
  }

  private void map(DynamicLayer layer, DynamicEntity entity) {
    mEntityToLayer.put(entity, layer);
    mGeoEntityToLayer.put(entity.getId(), layer);
  }
}
