package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class AlertPointEntity extends AlertEntity {

  private PointGeometry mPointGeometry;
  private AlertPointSymbol mSymbol;

  public AlertPointEntity(String id,
      String text,
      int severity,
      PointGeometry point,
      AlertPointSymbol symbol) {
    super(id, text, severity);

    mPointGeometry = point;
    mSymbol = symbol;
  }

  @Override
  public void accept(IGeoEntityVisitor visitor) {
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
