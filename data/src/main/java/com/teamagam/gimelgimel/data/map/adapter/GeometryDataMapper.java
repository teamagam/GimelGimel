package com.teamagam.gimelgimel.data.map.adapter;

import com.teamagam.gimelgimel.data.map.entity.Coordinate;
import com.teamagam.gimelgimel.data.map.entity.GeometryData;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.map.entity.PolygonData;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.ArrayList;
import java.util.Collection;
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

    public List<Geometry> transform(Collection<GeometryData> geometryDatas) {
        List<Geometry> geometries = new ArrayList<>(20);
        Geometry geometry;
        for (GeometryData geometryData : geometryDatas) {
            geometry = transform(geometryData);
            if (geometry != null) {
                geometries.add(geometry);
            }
        }

        return geometries;
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

    private GeometryData transformToData(Geometry location) {
        return new GeometryToDataTransformer().transformToData(location);
    }

    private class GeometryToDataTransformer implements IGeometryVisitor {

        GeometryData mGeometryData;

        private GeometryData transformToData(Geometry geo) {
            geo.accept(this);
            return mGeometryData;
        }

        @Override
        public void visit(PointGeometry geometry) {
            double latitude = geometry.getLatitude();
            double longitude = geometry.getLongitude();
            if (geometry.hasAltitude()) {
                mGeometryData = new PointGeometryData(latitude, longitude, geometry.getAltitude());
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
                        (float) point.getLatitude(),
                        (float) point.getLongitude());
            }
            mGeometryData = new PolygonData(coordinates);
        }
    }
}
