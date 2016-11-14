package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.Collection;

import rx.Observable;

/**
 * repository to be synced with the app for shown entities on the map
 */

public interface DisplayedEntitiesRepository {

    Observable<GeoEntityNotification> getSyncEntitiesObservable();

    Observable<Collection<GeoEntity>> getDisplayedGeoEntitiesObservable();

    void show(GeoEntity geoEntity);

    void hide(GeoEntity geoEntity);

    boolean isNotShown(GeoEntity geoEntity);
}
