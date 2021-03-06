package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class AlertPointEntity extends AlertEntity {

  private PointGeometry mPointGeometry;
  private AlertPointSymbol mSymbol;

  public AlertPointEntity(String id, int severity, PointGeometry point, AlertPointSymbol symbol) {
    super(id, severity);
    mPointGeometry = point;
    mSymbol = symbol;
  }

  @Override
  public void accept(GeoEntityVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public PointGeometry getGeometry() {
    return mPointGeometry;
  }

  @Override
  public Symbol getSymbol() {
    return mSymbol;
  }
}
