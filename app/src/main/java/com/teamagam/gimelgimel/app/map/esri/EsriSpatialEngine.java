package com.teamagam.gimelgimel.app.map.esri;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

import javax.inject.Inject;

public class EsriSpatialEngine implements SpatialEngine {

    @Inject
    public EsriSpatialEngine() {

    }

    @Override
    public double distanceInMeters(PointGeometry point1, PointGeometry point2) {
        return GeometryEngine.geodesicDistance(
                (Point) EsriUtils.transform(point1),
                (Point) EsriUtils.transform(point2),
                EsriUtils.WGS_84_GEO,
                (LinearUnit) LinearUnit.create(LinearUnit.Code.METER));
    }

    @Override
    public PointGeometry projectToUTM(PointGeometry pointGeometry) {
        return project(pointGeometry, EsriUtils.WGS_84_GEO, EsriUtils.ED50_UTM_36_N);
    }

    @Override
    public PointGeometry projectFromUTM(PointGeometry pointGeometry) {
        return project(pointGeometry, EsriUtils.ED50_UTM_36_N, EsriUtils.WGS_84_GEO);
    }

    private PointGeometry project(
            PointGeometry pointGeometry, SpatialReference from, SpatialReference to) {
        Point point = (Point) EsriUtils.transformAndProject(pointGeometry, from, to);
        return new PointGeometry(point.getY(), point.getX());
    }
}
