package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
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

    public GeoEntity transform(String id, GeoContentData geoContentData){
        GeoEntity geoEntity = null;
        if(id != null){
            geoEntity = mGeoEntitiesRepository.get(id);
        }
        if (geoEntity == null){
            geoEntity = createGeoEntity(id, geoContentData);
        }
        return geoEntity;
    }

    public GeoContentData transform(GeoEntity geoContentData) {
        return new GeoContentToDataTransformer().transform(geoContentData);
    }

    private GeoEntity createGeoEntity(String id, GeoContentData geoContentData) {
        //todo: replace in the future with several types of GeoEntities
        return new PointEntity(id, null,
                (PointGeometry) mGeometryMapper.transform(geoContentData.getGeometry()),
                new PointSymbol(geoContentData.getType(), geoContentData.getText()));
    }

    private class GeoContentToDataTransformer implements IGeoEntityVisitor {

        GeoContentData mGeoContentData;

        private GeoContentData transform(GeoEntity geoEntity) {
            geoEntity.accept(this);
            return mGeoContentData;
        }

        public void visit(PointEntity pointEntity) {
            mGeoContentData = new GeoContentData(mGeometryMapper.transformToData(pointEntity.getGeometry()),
                    pointEntity.getPointSymbol().getText(), pointEntity.getPointSymbol().getType());
        }

    }

}
