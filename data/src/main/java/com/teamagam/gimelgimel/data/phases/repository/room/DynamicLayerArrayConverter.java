package com.teamagam.gimelgimel.data.phases.repository.room;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.EntityToJsonConverter;

public class DynamicLayerArrayConverter extends EntityToJsonConverter<DynamicLayerEntity[]> {
  public DynamicLayerArrayConverter() {
    super(DynamicLayerEntity[].class);
  }
}
