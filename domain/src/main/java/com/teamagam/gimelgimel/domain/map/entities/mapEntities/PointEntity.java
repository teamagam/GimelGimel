package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;

/**
 * An entity class representing a point
 */
public class PointEntity extends AbsGeoEntity {

  private PointGeometry mPointGeometry;
  private PointSymbol mPointSymbol;

  public PointEntity(String id, String text, PointGeometry pointGeometry, PointSymbol pointSymbol) {
    super(id, text);
    mPointGeometry = pointGeometry;
    mPointSymbol = pointSymbol;
  }

  @Override
  public PointGeometry getGeometry() {
    return mPointGeometry;
  }

  @Override
  public PointSymbol getSymbol() {
    return mPointSymbol;
  }

  @Override
  public void accept(IGeoEntityVisitor visitor) {
    visitor.visit(this);
  }
}
