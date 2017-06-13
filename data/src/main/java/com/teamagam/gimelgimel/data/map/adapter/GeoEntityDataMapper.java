package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.geogson.core.model.Geometry;
import com.teamagam.geogson.core.model.LineString;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.Polygon;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeoEntityDataMapper {

  private final GeometryDataMapper mGeometryMapper;

  @Inject
  GeoEntityDataMapper(GeometryDataMapper geometryDataMapper) {
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
    return new ImageEntity(id, null, mGeometryMapper.transform(point), false);
  }

  public SensorEntity transformIntoSensorEntity(String id, String sensorName, Point point) {
    return new SensorEntity(id, sensorName, mGeometryMapper.transform(point), false);
  }

  public AlertEntity transformIntoAlertEntity(String id, String name, Geometry geo, int severity) {
    if (geo instanceof Point) {
      return transformToAlertPointEntity(id, name, geo, severity);
    } else if (geo instanceof com.teamagam.geogson.core.model.Polygon) {
      return transformToAlertPolygonEntity(id, name, geo, severity);
    } else {
      throw new RuntimeException("Unknown GeoContentData type, couldn't create geo-entity");
    }
  }

  public GeoContentData transform(GeoEntity geoEntity) {
    return new EntityToGeoContentDataTransformer().transform(geoEntity);
  }

  private PolygonEntity transformToPolygonEntity(String id, GeoContentData geoContentData) {
    return new PolygonEntity(id, geoContentData.getText(), mGeometryMapper.transform(
        (com.teamagam.geogson.core.model.Polygon) geoContentData.getGeometry()),
        new PolygonSymbol(false));
  }

  private PolylineEntity transformToPolylineEntity(String id, GeoContentData geoContentData) {
    return new PolylineEntity(id, geoContentData.getText(), mGeometryMapper.transform(
        (com.teamagam.geogson.core.model.LineString) geoContentData.getGeometry()),
        new PolylineSymbol(false));
  }

  private PointEntity transformToPointEntity(String id, GeoContentData geoContentData) {
    return new PointEntity(id, geoContentData.getText(),
        mGeometryMapper.transform((Point) geoContentData.getGeometry()),
        new PointSymbol(false, geoContentData.getLocationType()));
  }

  private AlertEntity transformToAlertPointEntity(String id,
      String name,
      Geometry geo,
      int severity) {
    return new AlertPointEntity(id, name, severity, mGeometryMapper.transform((Point) geo),
        new AlertPointSymbol(false));
  }

  private AlertEntity transformToAlertPolygonEntity(String id,
      String name,
      Geometry geo,
      int severity) {
    return new AlertPolygonEntity(id, name, severity, mGeometryMapper.transform((Polygon) geo),
        new AlertPolygonSymbol(false));
  }

  private class EntityToGeoContentDataTransformer implements IGeoEntityVisitor {

    GeoContentData mGeoContentData;

    private GeoContentData transform(GeoEntity geoEntity) {
      geoEntity.accept(this);
      return mGeoContentData;
    }

    @Override
    public void visit(PointEntity pointEntity) {
      mGeoContentData =
          new GeoContentData(mGeometryMapper.transformToData(pointEntity.getGeometry()),
              pointEntity.getText(), pointEntity.getSymbol().getType());
    }

    @Override
    public void visit(ImageEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText(), "Image");
    }

    @Override
    public void visit(UserEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText(), String.valueOf(entity.getSymbol().isActive()));
    }

    @Override
    public void visit(SensorEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText());
    }

    @Override
    public void visit(AlertPointEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText());
    }

    @Override
    public void visit(AlertPolygonEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText());
    }

    @Override
    public void visit(PolygonEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText());
    }

    @Override
    public void visit(PolylineEntity entity) {
      mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(entity.getGeometry()),
          entity.getText());
    }

    @Override
    public void visit(MyLocationEntity entity) {
      throw new RuntimeException("shouldn't be executed");
    }
  }
}
