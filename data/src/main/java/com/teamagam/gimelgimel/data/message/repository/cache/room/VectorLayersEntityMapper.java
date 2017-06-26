package com.teamagam.gimelgimel.data.message.repository.cache.room;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorLayersEntityMapper {

  @Inject
  public VectorLayersEntityMapper() {
  }

  public VectorLayer convertToDomain(VectorLayerEntity entity) {
    VectorLayer.Category category = VectorLayer.Category.values()[entity.category];
    VectorLayer.Severity severity = VectorLayer.Severity.values()[entity.severity];

    return new VectorLayer(entity.id, entity.layerName, entity.url, severity, category,
        entity.version);
  }

  public VectorLayerEntity convertToEntity(VectorLayer vectorLayer) {
    VectorLayerEntity entity = new VectorLayerEntity();

    entity.id = vectorLayer.getId();
    entity.layerName = vectorLayer.getName();
    entity.url = vectorLayer.getUrl();
    entity.severity = vectorLayer.getSeverity().ordinal();
    entity.category = vectorLayer.getCategory().ordinal();
    entity.version = vectorLayer.getVersion();

    return entity;
  }
}
