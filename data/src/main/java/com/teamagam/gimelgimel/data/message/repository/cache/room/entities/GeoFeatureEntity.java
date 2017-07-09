package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import com.teamagam.geogson.core.model.Geometry;

public class GeoFeatureEntity {
  public String id;
  public Geometry geometry;
  public String text;
  public Style style;

  public static class Style {
    public String iconId;
    public String iconTint;
    public String borderColor;
    public String borderStyle;
    public String fillColor;
  }
}
