package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

public class UserEntity extends AbsGeoEntity {

  private PointGeometry mPointGeometry;
  private UserSymbol mUserSymbol;

  public UserEntity(String id, PointGeometry pointGeometry, UserSymbol userSymbol) {
    super(id);
    mPointGeometry = pointGeometry;
    mUserSymbol = userSymbol;
  }

  @Override
  public PointGeometry getGeometry() {
    return mPointGeometry;
  }

  @Override
  public UserSymbol getSymbol() {
    return mUserSymbol;
  }

  @Override
  public void accept(GeoEntityVisitor visitor) {
    visitor.visit(this);
  }
}
