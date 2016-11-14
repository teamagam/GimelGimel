package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created on 11/1/2016.
 * TODO: complete text
 */
@Singleton
public class GeoEntityDataMapper {

    @Inject
    GeometryDataMapper mGeometryMapper;

    @Inject
    GeoEntitiesRepository mGeoEntitiesRepository;

    @Inject
    GeoEntityDataMapper() {
    }

    public String transformAndStore(String id, GeoContentData geoContentData) {
        GeoEntity geoEntity = mGeoEntitiesRepository.get(id);
        if (geoEntity == null){
            geoEntity = createGeoEntity(id, geoContentData);
            geoEntity.setLayerTag(Constants.RECEIVED_MESSAGES_GEO_ENTITIES_LAYER_TAG);
            mGeoEntitiesRepository.add(geoEntity);
        }
        return geoEntity.getId();
    }

    public String transformImageEntityAndStore(String id, PointGeometryData point){
        GeoEntity geoEntity = mGeoEntitiesRepository.get(id);
        if (geoEntity == null){
            geoEntity = new ImageEntity(id, null, mGeometryMapper.transform(point));
            geoEntity.setLayerTag(Constants.RECEIVED_MESSAGES_GEO_ENTITIES_LAYER_TAG);
            mGeoEntitiesRepository.add(geoEntity);
        }
        return geoEntity.getId();
    }

    public GeoContentData transform(String geoEntityId) {
        GeoEntity geoEntity = mGeoEntitiesRepository.get(geoEntityId);
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

        public void visit(PointEntity pointEntity) {
            mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(pointEntity.getGeometry()),
                    pointEntity.getText(), pointEntity.getSymbol().getType());
        }

        @Override
        public void visit(ImageEntity point) {
            mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(point.getGeometry()),
                    "Image", "image type");
        }

    }

}
