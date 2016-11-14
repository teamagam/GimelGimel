package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.geometries.GeometryApp;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometryApp;

/**
 * An abstract class for entities who's geometry is
 * a collection of locations (i.e. polylines, polygons)
 */
public abstract class MultipleLocationsEntity extends AbsEntity {

    private MultiPointGeometryApp mPointsGeometry;

    public MultipleLocationsEntity(String id, String text, MultiPointGeometryApp pointsGeometry) {
        super(id, text);
        mPointsGeometry = pointsGeometry;
    }

    @Override
    public GeometryApp getGeometry() {
        return mPointsGeometry;
    }

    public static abstract class MultiPointGeometryBuilder<B extends MultiPointGeometryBuilder<B,E>,  E extends MultipleLocationsEntity> extends EntityBuilder<B,E>{

        public B setGeometry(MultiPointGeometryApp geometry) {
            mGeometry = geometry;
            return getThis();
        }
    }

}
