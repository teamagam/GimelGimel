package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.LineString;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.AreaPositions;
import com.teamagam.geogson.core.model.positions.LinearPositions;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
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
            List<PointGeometry> pointGeometries = extractPointGeometries(
                    geoData.positions().children());

            polygon = new Polygon(pointGeometries);
        }

        return polygon;
    }

    public Polyline transform(com.teamagam.geogson.core.model.LineString geoData) {
        Polyline polyline = null;
        if (geoData != null) {
            List<PointGeometry> pointGeometries = new ArrayList<>();

            for (SinglePosition sp : geoData.positions().children()) {
                double lat = sp.coordinates().getLat();
                double lon = sp.coordinates().getLon();
                double alt = sp.coordinates().getAlt();

                PointGeometry pg = createPointGeometry(lat, lon, alt);
                pointGeometries.add(pg);
            }

            polyline = new Polyline(pointGeometries);
        }

        return polyline;
    }

    public PointGeometry transform(Point geoData) {
        double lat = geoData.lat();
        double lon = geoData.lon();
        double alt = geoData.alt();

        return createPointGeometry(lat, lon, alt);
    }

    public Geometry transformSuperclass(com.teamagam.geogson.core.model.Geometry geoData) {
        if (geoData instanceof Point) {
            return transform((Point) geoData);
        } else if (geoData instanceof com.teamagam.geogson.core.model.Polygon) {
            return transform((com.teamagam.geogson.core.model.Polygon) geoData);
        } else if (geoData instanceof LineString) {
            return transform((LineString) geoData);
        } else {
            throw new RuntimeException("Unknown Geometry type, couldn't create geometry");
        }
    }

    public Point transformToData(PointGeometry point) {
        return (Point) transformToData((Geometry) point);
    }

    public com.teamagam.geogson.core.model.Polygon transformToData(Polygon polygon) {
        return (com.teamagam.geogson.core.model.Polygon) transformToData((Geometry) polygon);
    }

    public com.teamagam.geogson.core.model.LineString transformToData(Polyline polyline) {
        return (com.teamagam.geogson.core.model.LineString) transformToData((Geometry) polyline);
    }

    public com.teamagam.geogson.core.model.Geometry transformToDataSuperclass(Geometry geometry) {
        if (geometry instanceof PointGeometry) {
            return transformToData((PointGeometry) geometry);
        } else if (geometry instanceof Polygon) {
            return transformToData((Polygon) geometry);
        } else if (geometry instanceof Polyline) {
            return transformToData((Polyline) geometry);
        } else {
            throw new RuntimeException("Unknown Geometry type, couldn't create geometry");
        }
    }

    private com.teamagam.geogson.core.model.Geometry transformToData(Geometry geometry) {
        return new GeometryToDataTransformer().transformToData(geometry);
    }

    private PointGeometry createPointGeometry(double lat, double lon, double alt) {
        return Double.isNaN(alt) ?
                new PointGeometry(lat, lon) :
                new PointGeometry(lat, lon, alt);
    }

    private List<PointGeometry> extractPointGeometries(Iterable<LinearPositions> positions) {
        List<PointGeometry> pgs = new ArrayList<>();
        for (LinearPositions lp : positions) {
            for (SinglePosition sp : lp.children()) {
                double lat = sp.coordinates().getLat();
                double lon = sp.coordinates().getLon();
                double alt = sp.coordinates().getAlt();

                PointGeometry pg = createPointGeometry(lat, lon, alt);
                pgs.add(pg);
            }
        }
        return pgs;
    }

    private class GeometryToDataTransformer implements IGeometryVisitor {

        com.teamagam.geogson.core.model.Geometry mGeometryData;

        private com.teamagam.geogson.core.model.Geometry transformToData(Geometry geo) {
            geo.accept(this);
            return mGeometryData;
        }

        @Override
        public void visit(PointGeometry point) {
            Coordinates coordinates = Coordinates.of(point.getLongitude(), point.getLatitude());

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

            Iterable<LinearPositions> lpIter = getIterableLinearPositions(coordinates);
            AreaPositions areaPositions = new AreaPositions(lpIter);

            mGeometryData = new com.teamagam.geogson.core.model.Polygon(areaPositions);
        }

        @Override
        public void visit(Polyline polyline) {
            List<SinglePosition> sp = getSinglePositions(polyline.getPoints());
            LinearPositions lp = new LinearPositions(sp);

            mGeometryData = new LineString(lp);
        }

        private List<SinglePosition> getSinglePositions(List<PointGeometry> points) {
            List<SinglePosition> coordinates = new ArrayList<>(points.size());

            for (PointGeometry point : points) {
                coordinates.add(new SinglePosition(
                        Coordinates.of(point.getLongitude(), point.getLatitude())
                ));
            }

            return coordinates;
        }

        private Iterable<LinearPositions> getIterableLinearPositions(
                List<SinglePosition> coordinates) {
            LinearPositions linearPositions = new LinearPositions(coordinates);
            List<LinearPositions> lpList = new ArrayList<>(1);
            lpList.add(linearPositions);

            return lpList;
        }

        private void closeCoordinates(List<SinglePosition> coordinates) {
            coordinates.add(coordinates.get(0));
        }
    }
}
