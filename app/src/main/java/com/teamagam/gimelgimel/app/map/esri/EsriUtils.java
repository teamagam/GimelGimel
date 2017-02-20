package com.teamagam.gimelgimel.app.map.esri;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public class EsriUtils {

    public static Point transformAndProject(PointGeometry point,
                                            SpatialReference srcSR,
                                            SpatialReference dstSR) {
        return (Point) GeometryEngine.project(transform(point), srcSR, dstSR);
    }

    private static Point transform(PointGeometry point) {
        Point p;
        if (point.hasAltitude()) {
            p = new Point(point.getLongitude(), point.getLatitude(), point.getAltitude());
        } else {
            p = new Point(point.getLongitude(), point.getLatitude());
        }
        return p;
    }
}
