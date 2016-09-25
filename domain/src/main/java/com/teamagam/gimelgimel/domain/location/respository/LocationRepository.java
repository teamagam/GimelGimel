package com.teamagam.gimelgimel.domain.location.respository;

import com.teamagam.gimelgimel.domain.map.entities.PointGeometry;

public interface LocationRepository {
    PointGeometry getLocation();
}
