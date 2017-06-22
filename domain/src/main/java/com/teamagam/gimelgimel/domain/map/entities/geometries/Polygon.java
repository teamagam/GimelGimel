package com.teamagam.gimelgimel.domain.map.entities.geometries;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;
import java.util.List;

public class Polygon extends AbsPointsGeometry {

  public Polygon(List<PointGeometry> points) {
    super(points);
  }

  @Override
  public void accept(GeometryVisitor visitor) {
    visitor.visit(this);
  }
}