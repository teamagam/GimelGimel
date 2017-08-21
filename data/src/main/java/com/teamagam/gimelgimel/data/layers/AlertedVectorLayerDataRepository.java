package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.AlertedVectorLayerRepository;
import javax.inject.Inject;

public class AlertedVectorLayerDataRepository implements AlertedVectorLayerRepository {

  private final AlertedVectorLayerDao mAlertedVectorLayerDao;
  private final AlertedVectorLayerMapper mMapper;

  @Inject
  public AlertedVectorLayerDataRepository(AlertedVectorLayerDao alertedVectorLayerDao) {
    mAlertedVectorLayerDao = alertedVectorLayerDao;
    mMapper = new AlertedVectorLayerMapper();
  }

  @Override
  public void markAsAlerted(VectorLayer vectorLayer) {
    mAlertedVectorLayerDao.insert(mMapper.map(vectorLayer));
  }

  @Override
  public boolean isAlerted(VectorLayer vectorLayer) {
    return mAlertedVectorLayerDao.contains(vectorLayer.getId(), vectorLayer.getVersion());
  }

  private static class AlertedVectorLayerMapper {
    AlertedVectorLayerEntity map(VectorLayer vl) {
      AlertedVectorLayerEntity entity = new AlertedVectorLayerEntity();
      entity.id = vl.getId();
      entity.version = vl.getVersion();
      return entity;
    }
  }
}