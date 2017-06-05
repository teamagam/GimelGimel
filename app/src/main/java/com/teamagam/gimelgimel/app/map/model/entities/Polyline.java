package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.entities.visitors.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;

/**
 * An entity class representing a polyline
 */
public class Polyline extends MultipleLocationsEntity {

  private static final String sPolylinePrefix = "polyline";
  private PolylineSymbol mPolylineSymbol;

  protected Polyline(String id, String text, MultiPointGeometryApp pointsGeometry) {
    this(id, text, pointsGeometry, PolylineSymbol.DEFAULT);
  }

  protected Polyline(String id,
      String text,
      MultiPointGeometryApp pointsGeometry,
      PolylineSymbol polylineSymbol) {
    super(id, text, pointsGeometry);
    mPolylineSymbol = polylineSymbol;
  }

  @Override
  public SymbolApp getSymbol() {
    return mPolylineSymbol;
  }

  @Override
  public void accept(IEntitiesVisitor visitor) {
    visitor.visit(this);
  }

  public static class Builder extends MultiPointGeometryBuilder<Builder, Polyline> {

    @Override
    protected Builder getThis() {
      return this;
    }

    @Override
    protected String getIdPrefix() {
      return sPolylinePrefix;
    }

    @Override
    public Polyline build() {
      return new Polyline(mId, mText, (MultiPointGeometryApp) mGeometry, (PolylineSymbol) mSymbol);
    }
  }
}
