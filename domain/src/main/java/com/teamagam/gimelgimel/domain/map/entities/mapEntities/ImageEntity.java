package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public class ImageEntity extends AbsGeoEntity {

  private PointGeometry mPointGeometry;
  private ImageSymbol mImageSymbol;

  public ImageEntity(String id, PointGeometry pointGeometry, boolean isSelected) {
    super(id);
    mPointGeometry = pointGeometry;
    mImageSymbol = new ImageSymbol(isSelected);
  }

  @Override
  public PointGeometry getGeometry() {
    return mPointGeometry;
  }

  @Override
  public Symbol getSymbol() {
    return mImageSymbol;
  }

  @Override
  public void accept(GeoEntityVisitor visitor) {
    visitor.visit(this);
  }
}