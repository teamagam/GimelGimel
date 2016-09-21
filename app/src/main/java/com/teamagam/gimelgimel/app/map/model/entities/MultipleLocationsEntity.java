package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.geometries.Geometry;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometry;

/**
 * An abstract class for entities who's geometry is
 * a collection of locations (i.e. polylines, polygons)
 */
public abstract class MultipleLocationsEntity extends AbsEntity {

    private MultiPointGeometry mPointsGeometry;

    public MultipleLocationsEntity(String id, MultiPointGeometry pointsGeometry) {
        super(id);
        mPointsGeometry = pointsGeometry;
    }

    @Override
    public Geometry getGeometry() {
        return mPointsGeometry;
    }

    @Override
    public void updateGeometry(Geometry geo) {
        if (!(geo instanceof MultiPointGeometry)) {
            throw new UnsupportedOperationException(
                    "Given geometry is not supported for entities of type " + this.getClass().getSimpleName());
        }

        mPointsGeometry = (MultiPointGeometry) geo;
        fireEntityChanged();
    }

    public static abstract class MultiPointGeometryBuilder<B extends MultiPointGeometryBuilder<B,E>,  E extends MultipleLocationsEntity> extends EntityBuilder<B,E>{

        public B setGeometry(MultiPointGeometry geometry) {
            mGeometry = geometry;
            return getThis();
        }
    }

}