package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.VectorLayer;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import rx.Observable;

/**
  * repository to be synced with the app for shown entities on the map
 */

public interface DisplayedEntitiesRepository {

    Observable<GeoEntityNotification> getSyncEntitiesObservable();

    Observable<GeoEntityNotification> getDisplayedVectorLayerObservable();
}
