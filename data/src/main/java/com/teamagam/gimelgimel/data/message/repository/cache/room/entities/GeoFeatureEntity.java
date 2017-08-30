package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import com.teamagam.geogson.core.model.Geometry;

public class GeoFeatureEntity {
  public String id;
  public Geometry geometry;
  public Style style;

  @Override
  public String toString() {
    return String.format("GeoFeatureEntity, id: %s\ngeometry: %s\nstyle: %s", id, geometry, style);
  }

  public static class Style {
    public String iconId;
    public String iconTint;
    public String borderColor;
    public String borderStyle;
    public String fillColor;

    @Override
    public String toString() {
      return String.format(
          "Style, iconId: %s\n, iconTint: %s\n, borderColor: %s\n, borderStyle: %s\n, fillColor: %s\n",
          iconId, iconTint, borderColor, borderStyle, fillColor);
    }
  }
}
