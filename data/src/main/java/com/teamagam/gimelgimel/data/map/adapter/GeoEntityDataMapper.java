package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.map.entity.PolygonData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;

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
        return createGeoEntity(id, geoContentData);
    }

    public ImageEntity transformIntoImageEntity(String id, PointGeometryData point) {
        return new ImageEntity(id, null, mGeometryMapper.transform(point),
                false);
    }

    public SensorEntity transformIntoSensorEntity(String id, String sensorName,
                                                  PointGeometryData point) {
        return new SensorEntity(id, sensorName,
                mGeometryMapper.transform(point), false);
    }


    public AlertEntity transformIntoAlertEntity(String id, String name,
                                                PointGeometryData point, int severity) {
        return new AlertEntity(id, name,
                mGeometryMapper.transform(point), severity, false);
    }

    public GeoContentData transform(GeoEntity geoEntity) {
        return new EntityToGeoContentDataTransformer().transform(geoEntity);
    }

    private GeoEntity createGeoEntity(String id, GeoContentData geoContentData) {
        if (geoContentData.getGeometry() instanceof PointGeometryData) {
            return new PointEntity(id, geoContentData.getText(),
                    (PointGeometry) mGeometryMapper.transform(geoContentData.getGeometry()),
                    new PointSymbol(false, geoContentData.getLocationType()));
        } else if (geoContentData.getGeometry() instanceof PolygonData) {
            return new PolygonEntity(id, geoContentData.getText(),
                    (Polygon) mGeometryMapper.transform(geoContentData.getGeometry()),
                    new PolygonSymbol(false));
        } else {
            throw new RuntimeException("Unknown GeoContentData type, couldn't create geo-entity");
        }
    }

    private class EntityToGeoContentDataTransformer implements IGeoEntityVisitor {

        GeoContentData mGeoContentData;

        private GeoContentData transform(GeoEntity geoEntity) {
            geoEntity.accept(this);
            return mGeoContentData;
        }

        @Override
        public void visit(PointEntity pointEntity) {
            mGeoContentData = new GeoContentData(
                    mGeometryMapper.transformToData(pointEntity.getGeometry()),
                    pointEntity.getText(), pointEntity.getSymbol().getType());
        }

        @Override
        public void visit(ImageEntity entity) {
            mGeoContentData = new GeoContentData(
                    mGeometryMapper.transformToData(entity.getGeometry()),
                    entity.getText(), "Image");
        }

        @Override
        public void visit(UserEntity entity) {
            mGeoContentData = new GeoContentData(
                    mGeometryMapper.transformToData(entity.getGeometry()),
                    entity.getText(), String.valueOf(entity.getSymbol().isActive()));
        }

        @Override
        public void visit(SensorEntity entity) {
            mGeoContentData = new GeoContentData(
                    mGeometryMapper.transformToData(entity.getGeometry()),
                    entity.getText(), null);
        }

        @Override
        public void visit(AlertEntity entity) {
            mGeoContentData = new GeoContentData(
                    mGeometryMapper.transformToData(entity.getGeometry()),
                    entity.getText(), null);
        }

        @Override
        public void visit(PolygonEntity entity) {
            mGeoContentData = new GeoContentData(
                    mGeometryMapper.transformToData(entity.getGeometry()),
                    entity.getText());
        }

        @Override
        public void visit(MyLocationEntity entity) {
            throw new RuntimeException("shouldn't be executed");
        }
    }
}
