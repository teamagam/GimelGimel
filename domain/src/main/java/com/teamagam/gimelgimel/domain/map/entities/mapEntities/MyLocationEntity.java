package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class MyLocationEntity extends AbsGeoEntity {

  private MyLocationSymbol mMyLocationSymbol;
  private PointGeometry mPointGeometry;

  public MyLocationEntity(String id,
      String text,
      MyLocationSymbol myLocationSymbol,
      PointGeometry pointGeometry) {
    super(id, text);
    mMyLocationSymbol = myLocationSymbol;
    mPointGeometry = pointGeometry;
  }

  @Override
  public PointGeometry getGeometry() {
    return mPointGeometry;
  }

  @Override
  public Symbol getSymbol() {
    return mMyLocationSymbol;
  }

  @Override
  public void accept(GeoEntityVisitor visitor) {
    visitor.visit(this);
  }
}
