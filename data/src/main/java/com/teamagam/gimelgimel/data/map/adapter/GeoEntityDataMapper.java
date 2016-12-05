package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created on 11/1/2016.
 */
@Singleton
public class GeoEntityDataMapper {

    @Inject
    GeometryDataMapper mGeometryMapper;

    @Inject
    GeoEntityDataMapper() {
    }

    public GeoEntity transform(String id, GeoContentData geoContentData) {
        GeoEntity geoEntity = createGeoEntity(id, geoContentData);
        geoEntity.setLayerTag(Constants.RECEIVED_MESSAGES_GEO_ENTITIES_LAYER_TAG);
        return geoEntity;
    }

    public ImageEntity transformIntoImageEntity(String id, PointGeometryData point) {
        ImageEntity imageEntity = new ImageEntity(id, null, mGeometryMapper.transform(point));
        imageEntity.setLayerTag(Constants.RECEIVED_MESSAGES_GEO_ENTITIES_LAYER_TAG);
        return imageEntity;
    }

    public SensorEntity transformIntoSensorEntity(String id, String sensorName,
                                                  PointGeometryData point) {
        SensorEntity sensorEntity = new SensorEntity(id, sensorName,
                mGeometryMapper.transform(point));
        sensorEntity.setLayerTag(Constants.SENSOR_LAYER_TAG);
        return sensorEntity;
    }


    public GeoContentData transform(GeoEntity geoEntity) {
        return new GeoContentToDataTransformer().transform(geoEntity);
    }

    private GeoEntity createGeoEntity(String id, GeoContentData geoContentData) {
        //todo: replace in the future with several types of GeoEntities
        return new PointEntity(id, geoContentData.getText(),
                (PointGeometry) mGeometryMapper.transform(geoContentData.getGeometry()),
                new PointSymbol(geoContentData.getType()));
    }

    private class GeoContentToDataTransformer implements IGeoEntityVisitor {

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
        public void visit(MyLocationEntity entity) {
            throw new RuntimeException("shouldn't be executed");
        }
    }
}
