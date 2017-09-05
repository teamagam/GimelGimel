package com.teamagam.gimelgimel.data.dynamicLayers.room.mapper;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicEntityDbEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.EntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import javax.inject.Inject;

public class DynamicEntityMapper implements EntityMapper<DynamicEntity, DynamicEntityDbEntity> {

  private final GeoFeatureEntityMapper mGeoFeatureEntityMapper;

  @Inject
  public DynamicEntityMapper(GeoFeatureEntityMapper geoFeatureEntityMapper) {
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
  }

  @Override
  public DynamicEntity mapToDomain(DynamicEntityDbEntity entity) {
    GeoFeatureEntity geoFeatureEntity = entity.geoFeatureEntity;
    GeoEntity geoEntity = mGeoFeatureEntityMapper.mapToDomain(geoFeatureEntity);
    return new DynamicEntity(geoEntity, entity.description);
  }

  @Override
  public DynamicEntityDbEntity mapToEntity(DynamicEntity entity) {
    DynamicEntityDbEntity dbEntity = new DynamicEntityDbEntity();
    dbEntity.description = entity.getDescription();
    dbEntity.geoFeatureEntity = mGeoFeatureEntityMapper.mapToEntity(entity.getGeoEntity());
    return dbEntity;
  }
}