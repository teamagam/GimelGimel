package com.teamagam.gimelgimel.app.view.viewer.data.geometries;

import java.util.Collection;

/**
 * Created by Bar on 03-Mar-16.
 */
public class MultiPointGeometry implements Geometry{
    public Collection<PointGeometry> pointsCollection;

    public MultiPointGeometry(
            Collection<PointGeometry> pointsCollection) {
        this.pointsCollection = pointsCollection;
    }
}
