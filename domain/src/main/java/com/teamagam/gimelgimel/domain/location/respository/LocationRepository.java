package com.teamagam.gimelgimel.domain.location.respository;

import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

public interface LocationRepository {
    PointGeometry getLocation();
}
