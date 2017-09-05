package com.teamagam.gimelgimel.data.dynamicLayers.room.mapper;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicEntityDbEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.EntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicLayersEntityMapper implements EntityMapper<DynamicLayer, DynamicLayerEntity> {

  private DynamicEntityMapper mDynamicEntityMapper;

  @Inject
  public DynamicLayersEntityMapper(DynamicEntityMapper dynamicEntityMapper) {
    mDynamicEntityMapper = dynamicEntityMapper;
  }

  public DynamicLayer mapToDomain(DynamicLayerEntity entity) {
    if (entity == null) {
      return null;
    }
    List<DynamicEntity> entities = extractEntities(entity);
    return new DynamicLayer(entity.id, entity.name, entity.description, entity.timestamp, entities);
  }

  public DynamicLayerEntity mapToEntity(DynamicLayer dynamicLayer) {
    if (dynamicLayer == null) {
      return null;
    }

    DynamicLayerEntity entity = new DynamicLayerEntity();
    entity.id = dynamicLayer.getId();
    entity.name = dynamicLayer.getName();
    entity.description = dynamicLayer.getDescription();
    entity.timestamp = dynamicLayer.getTimestamp();
    entity.entities = extractEntities(dynamicLayer.getEntities());

    return entity;
  }

  private List<DynamicEntity> extractEntities(DynamicLayerEntity entity) {
    if (entity.entities == null) {
      return null;
    }
    List<DynamicEntity> entities = new ArrayList();
    if (entity.entities != null) {
      for (int i = 0; i < entity.entities.length; i++) {
        entities.add(mDynamicEntityMapper.mapToDomain(entity.entities[i]));
      }
    }
    return entities;
  }

  private DynamicEntityDbEntity[] extractEntities(List<DynamicEntity> domainEntities) {
    if (domainEntities == null) {
      return null;
    }
    DynamicEntityDbEntity[] entities = new DynamicEntityDbEntity[domainEntities.size()];
    for (int i = 0; i < domainEntities.size(); i++) {
      DynamicEntity dynamicEntity = domainEntities.get(i);
      entities[i] = mDynamicEntityMapper.mapToEntity(dynamicEntity);
    }
    return entities;
  }
}
