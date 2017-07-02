package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;

public class PolygonEntity extends AbsGeoEntity {

  private final Polygon mPolygon;
  private final PolygonSymbol mSymbol;

  public PolygonEntity(String id, String text, Polygon polygon, PolygonSymbol symbol) {
    super(id, text);
    mPolygon = polygon;
    mSymbol = symbol;
  }

  @Override
  public Polygon getGeometry() {
    return mPolygon;
  }

  @Override
  public PolygonSymbol getSymbol() {
    return mSymbol;
  }

  @Override
  public void accept(GeoEntityVisitor visitor) {
    visitor.visit(this);
  }
}