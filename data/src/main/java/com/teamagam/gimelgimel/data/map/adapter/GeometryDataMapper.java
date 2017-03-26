package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.map.entity.Coordinate;
import com.teamagam.gimelgimel.data.map.entity.GeometryData;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.map.entity.PolygonData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeometryDataMapper {

    @Inject
    public GeometryDataMapper() {

    }

    public Geometry transform(GeometryData geoData) {
        Geometry geo = null;
        if (geoData != null) {
            geo = geoData.toModel();
        }

        return geo;
    }

    public PointGeometry transform(PointGeometryData point) {
        return (PointGeometry) transform((GeometryData) point);
    }

    public PointGeometryData transformToData(PointGeometry point) {
        return (PointGeometryData) transformToData((Geometry) point);
    }

    public PolygonData transformToData(Polygon polygon) {
        return (PolygonData) transformToData((Geometry) polygon);
    }

    private GeometryData transformToData(Geometry geometry) {
        return new GeometryToDataTransformer().transformToData(geometry);
    }

    private class GeometryToDataTransformer implements IGeometryVisitor {

        GeometryData mGeometryData;

        private GeometryData transformToData(Geometry geo) {
            geo.accept(this);
            return mGeometryData;
        }

        @Override
        public void visit(PointGeometry point) {
            double latitude = point.getLatitude();
            double longitude = point.getLongitude();
            if (point.hasAltitude()) {
                mGeometryData = new PointGeometryData(latitude, longitude, point.getAltitude());
            } else {
                mGeometryData = new PointGeometryData(latitude, longitude);
            }
        }

        @Override
        public void visit(Polygon polygon) {
            List<PointGeometry> points = polygon.getPoints();
            Coordinate[] coordinates = new Coordinate[points.size()];
            for (int i = 0; i < points.size(); i++) {
                PointGeometry point = points.get(i);
                coordinates[i] = new Coordinate(
                        (float) point.getLongitude(), (float) point.getLatitude()
                );
            }
            mGeometryData = new PolygonData(coordinates);
        }
    }
}
