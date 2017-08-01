package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public class MapDragEvent {

  private final PointGeometry mFrom;
  private final PointGeometry mTo;

  public MapDragEvent(PointGeometry from, PointGeometry to) {
    mFrom = from;
    mTo = to;
  }

  public PointGeometry getFrom() {
    return mFrom;
  }

  public PointGeometry getTo() {
    return mTo;
  }
}
