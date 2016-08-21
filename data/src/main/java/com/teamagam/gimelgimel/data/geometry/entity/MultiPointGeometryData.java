package com.teamagam.gimelgimel.data.geometry.entity;

import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;
import com.teamagam.gimelgimel.domain.geometries.entities.MultiPointGeometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Bar on 03-Mar-16.
 */
public class MultiPointGeometryData implements GeometryData {
    public Collection<PointGeometryData>
            pointsCollection;

    public MultiPointGeometryData(
            Collection<PointGeometryData> pointsCollection) {
        this.pointsCollection = pointsCollection;
    }

    @Override
    public Geometry transformToEntity() {
        Collection<PointGeometry> geoCollection = new LinkedList<>();
        for(PointGeometryData point : pointsCollection  ){
            geoCollection.add((PointGeometry) point.transformToEntity());
        }
        return new MultiPointGeometry(geoCollection);
    }
}