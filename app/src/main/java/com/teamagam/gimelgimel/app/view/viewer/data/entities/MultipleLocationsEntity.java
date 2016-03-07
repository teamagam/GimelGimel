package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;

/**
 * Created by Bar on 29-Feb-16.
 * <p/>
 * An abstract class for entities who's spatial data is
 * a collection of locations (i.e. polylines, polygons)
 */
public abstract class MultipleLocationsEntity extends AbsEntity {

    private MultiPointGeometry mPointsGeometry;

    public MultipleLocationsEntity(String id, MultiPointGeometry pointsGeometry) {
        super(id);
        this.mPointsGeometry = pointsGeometry;
    }

    public void updateLocations(MultiPointGeometry pointsGeometry) {
        mPointsGeometry = pointsGeometry;
        mEntityChangedListener.OnEntityChanged(this);
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
        this.mEntityChangedListener.OnEntityChanged(this);
    }
}
