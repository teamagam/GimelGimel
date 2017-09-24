package com.teamagam.gimelgimel.data.phases.room;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.EntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import java.util.List;
import javax.inject.Inject;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

public class PhaseLayerMapper implements EntityMapper<PhaseLayer, PhaseLayerEntity> {

  private final DynamicLayersEntityMapper mDynamicLayersEntityMapper;

  @Inject
  public PhaseLayerMapper(DynamicLayersEntityMapper dynamicLayersEntityMapper) {
    mDynamicLayersEntityMapper = dynamicLayersEntityMapper;
  }

  @Override
  public PhaseLayer mapToDomain(PhaseLayerEntity entity) {
    List<DynamicLayer> dynamicLayers = transform(asList(entity.phases), this::toDomain);
    return new PhaseLayer(entity.id, entity.name, entity.description, entity.timestamp,
        dynamicLayers);
  }

  @Override
  public PhaseLayerEntity mapToEntity(PhaseLayer layer) {
    PhaseLayerEntity entity = new PhaseLayerEntity();
    entity.id = layer.getId();
    entity.name = layer.getName();
    entity.description = layer.getDescription();
    entity.timestamp = layer.getTimestamp();
    entity.phases = mapPhases(layer);
    return entity;
  }

  private DynamicLayer toDomain(DynamicLayerEntity dynamicLayerEntity) {
    return mDynamicLayersEntityMapper.mapToDomain(dynamicLayerEntity);
  }

  private DynamicLayerEntity[] mapPhases(PhaseLayer layer) {
    return transform(layer.getPhases(), mDynamicLayersEntityMapper::mapToEntity).toArray(
        new DynamicLayerEntity[0]);
  }
}
