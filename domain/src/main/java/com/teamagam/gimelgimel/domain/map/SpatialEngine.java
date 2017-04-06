package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public interface SpatialEngine {

    double distanceInMeters(PointGeometry point1, PointGeometry point2);
}