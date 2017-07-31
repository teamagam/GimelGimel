package com.teamagam.gimelgimel.domain.notifications.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

/**
 * Notification for syncing map with Displayed repository
 */

public class GeoEntityNotification {
  public final static int ADD = 1;
  public final static int REMOVE = 2;
  public final static int UPDATE = 3;
  private GeoEntity mGeoEntity;
  private int mAction;

  private GeoEntityNotification(GeoEntity geoEntity, int action) {
    mGeoEntity = geoEntity;
    mAction = action;
  }

  public static GeoEntityNotification createAdd(GeoEntity entity) {
    return new GeoEntityNotification(entity, ADD);
  }

  public static GeoEntityNotification createRemove(GeoEntity entity) {
    return new GeoEntityNotification(entity, REMOVE);
  }

  public static GeoEntityNotification createUpdate(GeoEntity entity) {
    return new GeoEntityNotification(entity, UPDATE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GeoEntityNotification that = (GeoEntityNotification) o;

    if (mAction != that.mAction) return false;
    return mGeoEntity != null ? mGeoEntity.equals(that.mGeoEntity) : that.mGeoEntity == null;
  }

  @Override
  public int hashCode() {
    int result = mGeoEntity != null ? mGeoEntity.hashCode() : 0;
    result = 31 * result + mAction;
    return result;
  }

  public GeoEntity getGeoEntity() {
    return mGeoEntity;
  }

  public int getAction() {
    return mAction;
  }
}
