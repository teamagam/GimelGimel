package com.teamagam.gimelgimel.data.dynamicLayers.room.converter;

import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicEntityDbEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.EntityToJsonConverter;

public class DynamicEntityArrayConverter extends EntityToJsonConverter<DynamicEntityDbEntity[]> {

  public DynamicEntityArrayConverter() {
    super(DynamicEntityDbEntity[].class);
  }
}
