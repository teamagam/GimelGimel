package com.teamagam.gimelgimel.data.message.repository.cache.room.converters;

import com.teamagam.geogson.core.model.Point;

public class PointConverter extends EntityToJsonConverter<Point> {
  public PointConverter() {
    super(Point.class);
  }
}
