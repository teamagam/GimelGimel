package com.teamagam.gimelgimel.domain.map.entities.geometries;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsPointsGeometry implements Geometry {

  protected List<PointGeometry> mPoints;

  public AbsPointsGeometry(List<PointGeometry> points) {
    mPoints = new ArrayList<>(points);
  }

  public List<PointGeometry> getPoints() {
    return mPoints;
  }
}
