package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.geogson.core.model.Geometry;
import com.teamagam.geogson.core.model.LineString;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.Polygon;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.IconData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.Style;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeoEntityDataMapper {

  private final GeometryDataMapper mGeometryMapper;

  @Inject
  public GeoEntityDataMapper(GeometryDataMapper geometryDataMapper) {
    mGeometryMapper = geometryDataMapper;
  }

  public GeoEntity transform(String id, GeoContentData geoContentData) {
    com.teamagam.geogson.core.model.Geometry geo = geoContentData.getGeometry();
    if (geo instanceof Point) {
      return transformToPointEntity(id, geoContentData);
    } else if (geo instanceof com.teamagam.geogson.core.model.Polygon) {
      return transformToPolygonEntity(id, geoContentData);
    } else if (geo instanceof LineString) {
      return transformToPolylineEntity(id, geoContentData);
    } else {
      throw new RuntimeException("Unknown GeoContentData type, couldn't create geo-entity");
    }
  }

  public ImageEntity transformIntoImageEntity(String id, Point point) {
    return new ImageEntity(id, mGeometryMapper.transform(point), false);
  }

  public AlertEntity transformIntoAlertEntity(String id, String name, Geometry geo, int severity) {
    if (geo instanceof Point) {
      return transformToAlertPointEntity(id, geo, severity);
    } else if (geo instanceof com.teamagam.geogson.core.model.Polygon) {
      return transformToAlertPolygonEntity(id, geo, severity);
    } else {
      throw new RuntimeException("Unknown GeoContentData type, couldn't create geo-entity");
    }
  }

  public GeoContentData transform(GeoEntity geoEntity) {
    return new EntityToGeoContentDataTransformer().transform(geoEntity);
  }

  public GeoContentData transform(DynamicEntity dynamicEntity) {
    GeoContentData geoContentData = transform(dynamicEntity.getGeoEntity());
    geoContentData.setText(dynamicEntity.getDescription());
    return geoContentData;
  }

  private PolygonEntity transformToPolygonEntity(String id, GeoContentData geoContentData) {
    return new PolygonEntity(id, mGeometryMapper.transform(
        (com.teamagam.geogson.core.model.Polygon) geoContentData.getGeometry()),
        getPolygonSymbol(geoContentData.getStyle()));
  }

  private PolygonSymbol getPolygonSymbol(Style style) {
    PolygonSymbol.PolygonSymbolBuilder builder = new PolygonSymbol.PolygonSymbolBuilder();
    if (style != null) {
      builder.setBorderStyle(style.getBorderStyle())
          .setBorderColor(style.getBorderColor())
          .setFillColor(style.getFillColor());
    }
    return builder.build();
  }

  private PolylineEntity transformToPolylineEntity(String id, GeoContentData geoContentData) {
    return new PolylineEntity(id, mGeometryMapper.transform(
        (com.teamagam.geogson.core.model.LineString) geoContentData.getGeometry()),
        getPolylineSymbol(geoContentData.getStyle()));
  }

  private PolylineSymbol getPolylineSymbol(Style style) {
    PolylineSymbol.PolylineSymbolBuilder builder = new PolylineSymbol.PolylineSymbolBuilder();
    if (style != null) {
      builder.setBorderColor(style.getBorderColor()).setBorderStyle(style.getBorderStyle());
    }
    return builder.build();
  }

  private PointEntity transformToPointEntity(String id, GeoContentData geoContentData) {
    return new PointEntity(id, mGeometryMapper.transform((Point) geoContentData.getGeometry()),
        getPointSymbol(geoContentData.getStyle()));
  }

  private PointSymbol getPointSymbol(Style style) {
    PointSymbol.PointSymbolBuilder builder = new PointSymbol.PointSymbolBuilder();
    if (style != null) {
      builder.setIconId(style.getIconData().getIconId())
          .setTintColor(style.getIconData().getColor());
    }
    return builder.build();
  }

  private AlertEntity transformToAlertPointEntity(String id, Geometry geo, int severity) {
    return new AlertPointEntity(id, severity, mGeometryMapper.transform((Point) geo),
        new AlertPointSymbol(false));
  }

  private AlertEntity transformToAlertPolygonEntity(String id, Geometry geo, int severity) {
    return new AlertPolygonEntity(id, severity, mGeometryMapper.transform((Polygon) geo),
        new AlertPolygonSymbol(false));
  }

  private class EntityToGeoContentDataTransformer implements GeoEntityVisitor {

    Style mStyle;
    GeoContentData mGeoContentData;

    private GeoContentData transform(GeoEntity geoEntity) {
      new SymbolToStyleTransformer().transform(geoEntity.getSymbol());
      geoEntity.accept(this);
      return mGeoContentData;
    }

    @Override
    public void visit(PointEntity entity) {
      mGeoContentData =
          new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()), mStyle);
    }

    @Override
    public void visit(ImageEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()));
    }

    @Override
    public void visit(UserEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()));
    }

    @Override
    public void visit(AlertPointEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()));
    }

    @Override
    public void visit(AlertPolygonEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()));
    }

    @Override
    public void visit(PolygonEntity entity) {
      mGeoContentData =
          new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()), mStyle);
    }

    @Override
    public void visit(PolylineEntity entity) {
      mGeoContentData =
          new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()), mStyle);
    }

    private class SymbolToStyleTransformer implements ISymbolVisitor {

      public void transform(Symbol symbol) {
        symbol.accept(this);
      }

      @Override
      public void visit(PointSymbol symbol) {
        IconData iconData = new IconData(symbol.getIconId(), symbol.getTintColor());
        mStyle = new Style(iconData, null, null, null);
      }

      @Override
      public void visit(ImageSymbol symbol) {

      }

      @Override
      public void visit(UserSymbol symbol) {

      }

      @Override
      public void visit(AlertPointSymbol symbol) {

      }

      @Override
      public void visit(AlertPolygonSymbol symbol) {

      }

      @Override
      public void visit(PolygonSymbol symbol) {
        mStyle = new Style(null, symbol.getBorderColor(), symbol.getFillColor(),
            symbol.getBorderStyle());
      }

      @Override
      public void visit(PolylineSymbol symbol) {
        mStyle = new Style(null, symbol.getBorderColor(), null, symbol.getBorderStyle());
      }
    }
  }
}
