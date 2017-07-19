package com.teamagam.gimelgimel.data.dynamicLayers.room.mapper;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.EntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicLayersEntityMapper implements EntityMapper<DynamicLayer, DynamicLayerEntity> {

  @Inject
  public DynamicLayersEntityMapper() {
  }

  public DynamicLayer mapToDomain(DynamicLayerEntity entity) {
    return new DynamicLayer(entity.id, entity.name, Collections.EMPTY_LIST);
  }

  public DynamicLayerEntity mapToEntity(DynamicLayer dynamicLayer) {
    DynamicLayerEntity entity = new DynamicLayerEntity();

    entity.id = dynamicLayer.getId();
    entity.name = dynamicLayer.getName();

    return entity;
  }
}
