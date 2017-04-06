package com.teamagam.gimelgimel.app.map.esri;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.List;

public class EsriUtils {

    static final SpatialReference WGS_84_GEO = SpatialReference.create(
            SpatialReference.WKID_WGS84
    );

    public static com.esri.core.geometry.Geometry transformAndProject(Geometry geometry,
                                                                      SpatialReference srcSR,
                                                                      SpatialReference dstSR) {
        return GeometryEngine.project(
                transform(geometry),
                srcSR,
                dstSR);
    }

    public static com.esri.core.geometry.Geometry transform(Geometry geometry) {
        return DomainToEsriGeometryTransformer.transform(geometry);
    }


    private static class DomainToEsriGeometryTransformer implements IGeometryVisitor {

        static com.esri.core.geometry.Geometry transform(Geometry geometry) {
            DomainToEsriGeometryTransformer visitor = new DomainToEsriGeometryTransformer();
            geometry.accept(visitor);
            return visitor.mResult;
        }

        private com.esri.core.geometry.Geometry mResult;

        @Override
        public void visit(PointGeometry point) {
            mResult = transformPoint(point);
        }

        @Override
        public void visit(Polygon polygon) {
            mResult = transformPolygon(polygon);
        }

        @Override
        public void visit(Polyline polyline) {
            mResult = transformPolyline(polyline);
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

        private static com.esri.core.geometry.Polygon transformPolygon(Polygon polygon) {
            List<PointGeometry> points = polygon.getPoints();
            com.esri.core.geometry.Polygon resPolygon = new com.esri.core.geometry.Polygon();
            addPath(resPolygon, points);

            return resPolygon;
        }

        private static com.esri.core.geometry.Polyline transformPolyline(Polyline polyline) {
            List<PointGeometry> points = polyline.getPoints();
            com.esri.core.geometry.Polyline resPolyline = new com.esri.core.geometry.Polyline();
            addPath(resPolyline, points);

            return resPolyline;
        }

        private static void addPath(MultiPath path, List<PointGeometry> points) {
            path.startPath(transformPoint(points.get(0)));

            for (int i = 1; i < points.size(); i++) {
                path.lineTo(transformPoint(points.get(i)));
            }
        }
    }
}
