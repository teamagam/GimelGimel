package com.teamagam.gimelgimel.data.map.entity;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO class for GSON serialization and deserialization of a GeoJSON polygon geometry
 */
public class PolygonData implements GeometryData {

    @SerializedName("coordinates")
    public Coordinate[] coordinates;

    public PolygonData(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Polygon toModel() {
        List<PointGeometry> points = new ArrayList<>(coordinates.length);
        for (Coordinate coordinate : coordinates) {
            points.add(coordinate.toModel());
        }
        return new Polygon(points);
    }
}