package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;

public class PolylineEntity extends AbsGeoEntity {

  private final Polyline mPolyline;
  private final PolylineSymbol mSymbol;

  public PolylineEntity(String id, Polyline polyline, PolylineSymbol symbol) {
    super(id);
    mPolyline = polyline;
    mSymbol = symbol;
  }

  @Override
  public Polyline getGeometry() {
    return mPolyline;
  }

  @Override
  public PolylineSymbol getSymbol() {
    return mSymbol;
  }

  @Override
  public void accept(GeoEntityVisitor visitor) {
    visitor.visit(this);
  }
}
