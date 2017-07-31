package com.teamagam.gimelgimel.data.dynamicLayers.room.converter;

import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.EntityToJsonConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;

public class GeoFeatureEntityArrayConverter extends EntityToJsonConverter<GeoFeatureEntity[]> {

  public GeoFeatureEntityArrayConverter() {
    super(GeoFeatureEntity[].class);
  }
}
