package com.teamagam.gimelgimel.data.message.repository.cache.room.Converters;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public class PointGeometryConverter extends EntityToJsonConverter<PointGeometry> {
  public PointGeometryConverter() {
    super(PointGeometry.class);
  }
}
