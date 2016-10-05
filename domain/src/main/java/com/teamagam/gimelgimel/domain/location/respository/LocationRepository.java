package com.teamagam.gimelgimel.domain.location.respository;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public interface LocationRepository {
    PointGeometry getLocation();
}
