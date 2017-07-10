package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class PolylineEntity extends AbsGeoEntity {

  private final Polyline mPolyline;
  private final PolylineSymbol mSymbol;

  public PolylineEntity(String id, String text, Polyline polyline, PolylineSymbol symbol) {
    super(id, text);
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
