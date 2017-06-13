package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.entities.visitors.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.map.model.geometries.GeometryApp;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbolApp;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;

/**
 * An entity class representing a point
 */
public class Point extends AbsEntity {

  private static final String sPointPrefix = "point";
  private PointGeometryApp mPointGeometry;
  private PointSymbolApp mPointSymbol;
  private String mType;

  public Point(String id,
      String text,
      PointGeometryApp pointGeometry,
      PointSymbolApp pointSymbol,
      String type) {
    super(id, text);
    mType = type;
    mPointGeometry = pointGeometry;
    mPointSymbol = pointSymbol;
  }

  @Override
  public GeometryApp getGeometry() {
    return mPointGeometry;
  }

  @Override
  public SymbolApp getSymbol() {
    return mPointSymbol;
  }

  @Override
  public void accept(IEntitiesVisitor visitor) {
    visitor.visit(this);
  }

  public String getTypeString() {
    return mType;
  }

  public static class Builder extends EntityBuilder<Builder, Point> {

    private String mType = null;

    @Override
    protected Builder getThis() {
      return this;
    }

    @Override
    protected String getIdPrefix() {
      return sPointPrefix;
    }

    @Override
    public Point build() {
      return new Point(mId, mText, (PointGeometryApp) mGeometry, (PointSymbolApp) mSymbol, mType);
    }

    public Builder setStringType(String type) {
      mType = type;
      return this;
    }
  }
}
