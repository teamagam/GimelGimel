package com.teamagam.gimelgimel.domain.location.respository;

import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

import rx.Observable;

public interface LocationRepository {
    Observable<PointGeometry> getLocation();
}
