package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class AlertPolygonEntity extends AlertEntity {

  private Polygon mPolygon;
  private AlertPolygonSymbol mSymbol;

  public AlertPolygonEntity(String id,
      String text,
      int severity,
      Polygon polygon,
      AlertPolygonSymbol symbol) {
    super(id, text, severity);

    mPolygon = polygon;
    mSymbol = symbol;
  }

  @Override
  public void accept(IGeoEntityVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public Polygon getGeometry() {
    return mPolygon;
  }

  @Override
  public Symbol getSymbol() {
    return mSymbol;
  }
}
