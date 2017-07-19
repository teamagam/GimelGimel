package com.teamagam.gimelgimel.data.dynamicLayers.room.mapper;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.EntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicLayersEntityMapper implements EntityMapper<DynamicLayer, DynamicLayerEntity> {

  private GeoFeatureEntityMapper mGeoFeatureEntityMapper;

  @Inject
  public DynamicLayersEntityMapper(GeoFeatureEntityMapper geoFeatureEntityMapper) {
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
  }

  public DynamicLayer mapToDomain(DynamicLayerEntity entity) {
    if (entity == null) {
      return null;
    }
    List<GeoEntity> list = extractEntities(entity);
    return new DynamicLayer(entity.id, entity.name, list);
  }

  public DynamicLayerEntity mapToEntity(DynamicLayer dynamicLayer) {
    if (dynamicLayer == null) {
      return null;
    }

    DynamicLayerEntity entity = new DynamicLayerEntity();

    entity.id = dynamicLayer.getId();
    entity.name = dynamicLayer.getName();
    List<GeoEntity> domainEntities = dynamicLayer.getEntities();
    if (domainEntities == null) {
      entity.entities = null;
    } else {
      entity.entities = extractEntities(domainEntities);
    }
    return entity;
  }

  private List<GeoEntity> extractEntities(DynamicLayerEntity entity) {
    List<GeoEntity> list = new ArrayList();
    if (entity.entities != null) {
      for (int i = 0; i < entity.entities.length; i++) {
        list.add(mGeoFeatureEntityMapper.mapToDomain(entity.entities[i]));
      }
    }
    return list;
  }

  private GeoFeatureEntity[] extractEntities(List<GeoEntity> domainEntities) {
    GeoFeatureEntity[] entities = new GeoFeatureEntity[domainEntities.size()];
    for (int i = 0; i < domainEntities.size(); i++) {
      GeoEntity geoEntity = domainEntities.get(i);
      entities[i] = mGeoFeatureEntityMapper.mapToEntity(geoEntity);
    }
    return entities;
  }
}
