package com.teamagam.gimelgimel.app.map.model.geometries;

import java.util.Collection;

/**
 * Created by Bar on 03-Mar-16.
 */
public class MultiPointGeometryApp implements GeometryApp {
  public Collection<PointGeometryApp> pointsCollection;

  public MultiPointGeometryApp(Collection<PointGeometryApp> pointsCollection) {
    this.pointsCollection = pointsCollection;
  }
}
