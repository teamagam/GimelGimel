package com.teamagam.gimelgimel.domain.map.entities.geometries;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;
import java.util.List;

public class Polyline extends AbsPointsGeometry {

  public Polyline(List<PointGeometry> points) {
    super(points);
  }

  @Override
  public void accept(GeometryVisitor visitor) {
    visitor.visit(this);
  }
}
