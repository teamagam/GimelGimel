package com.teamagam.gimelgimel.data.timeplay.dynamic_layers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.List;
import javax.inject.Inject;

public class DynamicLayersSnapshoter implements GeoSnapshoter {

  private final DynamicLayerDao mDao;
  private final DynamicLayersEntityMapper mMapper;

  @Inject
  public DynamicLayersSnapshoter(DynamicLayerDao dao, DynamicLayersEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
  }

  @Override
  public List<GeoEntity> snapshot(long maxTimestamp) {
    List<DynamicLayerEntity> dynamicLayerEntities = mDao.getDynamicLayersUntil(maxTimestamp);
    List<DynamicLayer> dynamicLayers = Lists.transform(dynamicLayerEntities, mMapper::mapToDomain);
    List<List<DynamicEntity>> entitiesList = Lists.transform(dynamicLayers, dl -> dl.getEntities());
    Iterable<DynamicEntity> flattedEntities = Iterables.concat(entitiesList);
    List<DynamicEntity> dynamicEntities = Lists.newArrayList(flattedEntities);
    return Lists.transform(dynamicEntities, DynamicEntity::getGeoEntity);
  }
}
