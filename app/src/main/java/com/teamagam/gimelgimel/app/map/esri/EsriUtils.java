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

    private static final int ED50_UTM_36_N_WKID = 23036;

    public static final SpatialReference WGS_84_GEO =
            SpatialReference.create(SpatialReference.WKID_WGS84);
    public static final SpatialReference ED50_UTM_36_N =
            SpatialReference.create(ED50_UTM_36_N_WKID);

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

    public static PointGeometry projectDomainPoint(
            PointGeometry point, SpatialReference srcSR, SpatialReference dstSR) {
        Point pointEsri = (Point) transform(point);
        Point projectedPointEsri = (Point) GeometryEngine.project(pointEsri, srcSR, dstSR);
        return transformPoint(projectedPointEsri);
    }

    private static PointGeometry transformPoint(Point point) {
        if (point.getZ() > 0) {
            return new PointGeometry(point.getY(), point.getX(), point.getZ());
        } else {
            return new PointGeometry(point.getY(), point.getX());
        }
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
            if (point.hasAltitude()) {
                return new Point(point.getLongitude(), point.getLatitude(), point.getAltitude());
            } else {
                return new Point(point.getLongitude(), point.getLatitude());
            }
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
