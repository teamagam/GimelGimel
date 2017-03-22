package com.teamagam.gimelgimel.app.map.esri;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.List;

public class EsriUtils {

    public static com.esri.core.geometry.Geometry transformAndProject(Geometry geometry,
                                                                      SpatialReference srcSR,
                                                                      SpatialReference dstSR) {
        return GeometryEngine.project(
                DomainToEsriGeometryTransformer.transform(geometry),
                srcSR,
                dstSR);
    }


    private static class DomainToEsriGeometryTransformer implements IGeometryVisitor {

        static com.esri.core.geometry.Geometry transform(Geometry geometry) {
            DomainToEsriGeometryTransformer visitor = new DomainToEsriGeometryTransformer();
            geometry.accept(visitor);
            return visitor.mResult;
        }

        private com.esri.core.geometry.Geometry mResult;

        @Override
        public void visit(PointGeometry geometry) {
            mResult = transformPoint(geometry);
        }

        @Override
        public void visit(Polygon polygon) {
            mResult = transformPolygon(polygon);
        }

        private static Point transformPoint(PointGeometry point) {
            Point p;
            if (point.hasAltitude()) {
                p = new Point(point.getLongitude(), point.getLatitude(), point.getAltitude());
            } else {
                p = new Point(point.getLongitude(), point.getLatitude());
            }
            return p;
        }

        private com.esri.core.geometry.Polygon transformPolygon(Polygon polygon) {
            List<PointGeometry> points = polygon.getPoints();
            com.esri.core.geometry.Polygon resPolygon = new com.esri.core.geometry.Polygon();
            resPolygon.startPath(points.get(0).getLongitude(), points.get(0).getLatitude());
            for (int i = 1; i < points.size(); i++) {
                resPolygon.lineTo(points.get(i).getLongitude(), points.get(i).getLatitude());
            }
            return resPolygon;
        }
    }
}
