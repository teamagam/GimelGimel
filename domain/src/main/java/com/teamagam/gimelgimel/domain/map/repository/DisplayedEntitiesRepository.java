package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import io.reactivex.Observable;

/**
 * repository to be synced with the app for shown entities on the map
 */

public interface DisplayedEntitiesRepository {

  Observable<GeoEntityNotification> getObservable();

  void show(GeoEntity geoEntity);

  void hide(GeoEntity geoEntity);

  boolean isShown(GeoEntity geoEntity);
}
