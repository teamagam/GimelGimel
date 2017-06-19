package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;

public class GeoFeatureEntityConverter extends EntityToJsonConverter<GeoFeatureEntity> {

  public GeoFeatureEntityConverter() {
    super(GeoFeatureEntity.class);
  }
}
