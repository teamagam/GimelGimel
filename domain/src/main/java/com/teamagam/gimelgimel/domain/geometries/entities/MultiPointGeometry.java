package com.teamagam.gimelgimel.domain.geometries.entities;

import com.teamagam.gimelgimel.domain.geometries.entities.interfaces.IGeometryVisitor;

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

    @Override
    public void accept(IGeometryVisitor visitor) {
        visitor.visit(this);
    }

}
