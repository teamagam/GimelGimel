package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.AreaPositions;
import com.teamagam.geogson.core.model.positions.LinearPositions;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeometryDataMapper {

    @Inject
    public GeometryDataMapper() {

    }

    public Polygon transform(com.teamagam.geogson.core.model.Polygon geoData) {
        Polygon polygon = null;
        if (geoData != null) {
            List<PointGeometry> pointGeometries = new ArrayList<>();

            for (LinearPositions lp : geoData.positions().children()) {
                for (SinglePosition sp : lp.children()) {
                    double lat = sp.coordinates().getLat();
                    double lon = sp.coordinates().getLon();
                    double alt = sp.coordinates().getAlt();

                    PointGeometry pg = createPointGeometry(lat, lon, alt);
                    pointGeometries.add(pg);
                }
            }

            polygon = new Polygon(pointGeometries);
        }

        return polygon;
    }

    public PointGeometry transform(Point geoData) {
        double lat = geoData.lat();
        double lon = geoData.lon();
        double alt = geoData.alt();

        return createPointGeometry(lat, lon, alt);
    }

    public Point transformToData(PointGeometry point) {
        return (Point) transformToData((Geometry) point);
    }

    public com.teamagam.geogson.core.model.Polygon transformToData(Polygon polygon) {
        return (com.teamagam.geogson.core.model.Polygon) transformToData((Geometry) polygon);
    }

    private com.teamagam.geogson.core.model.Geometry transformToData(Geometry geometry) {
        return new GeometryToDataTransformer().transformToData(geometry);
    }

    private PointGeometry createPointGeometry(double lat, double lon, double alt) {
        return Double.isNaN(alt) ?
                new PointGeometry(lat, lon) :
                new PointGeometry(lat, lon, alt);
    }

    private class GeometryToDataTransformer implements IGeometryVisitor {

        com.teamagam.geogson.core.model.Geometry mGeometryData;

        private com.teamagam.geogson.core.model.Geometry transformToData(Geometry geo) {
            geo.accept(this);
            return mGeometryData;
        }

        @Override
        public void visit(PointGeometry point) {
            double latitude = point.getLatitude();
            double longitude = point.getLongitude();
            Coordinates coordinates = Coordinates.of(latitude, longitude);

            if (point.hasAltitude()) {
                coordinates = coordinates.withAlt(point.getAltitude());
            }

            SinglePosition singlePosition = new SinglePosition(coordinates);
            mGeometryData = new Point(singlePosition);
        }

        @Override
        public void visit(Polygon polygon) {
            List<PointGeometry> points = polygon.getPoints();
            List<SinglePosition> coordinates = getSinglePositions(points);
            closeCoordinates(coordinates);

            List<LinearPositions> lpList = getLinearPositions(coordinates);
            AreaPositions areaPositions = new AreaPositions(lpList);

            mGeometryData = new com.teamagam.geogson.core.model.Polygon(areaPositions);
        }

        private List<SinglePosition> getSinglePositions(List<PointGeometry> points) {
            List<SinglePosition> coordinates = new ArrayList<>();

            for (int i = 0; i < points.size(); i++) {
                PointGeometry point = points.get(i);
                coordinates.add(new SinglePosition(
                        Coordinates.of(point.getLongitude(), point.getLatitude())
                ));
            }

            return coordinates;
        }

        private List<LinearPositions> getLinearPositions(List<SinglePosition> coordinates) {
            LinearPositions linearPositions = new LinearPositions(coordinates);
            List<LinearPositions> lpList = new ArrayList<>();
            lpList.add(linearPositions);

            return lpList;
        }

        private void closeCoordinates(List<SinglePosition> coordinates) {
            coordinates.add(coordinates.get(0));
        }
    }
}
