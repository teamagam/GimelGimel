package com.teamagam.gimelgimel.domain.notifications.entity;

import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;

/**
 * Notification for syncing map with Displayed repository
 */

public class GeoEntityNotification {

    public final static int ADD = 1;
    public final static int REMOVE = 2;
    public final static int UPDATE = 3;

    public static GeoEntityNotification createAdd(GeoEntity entity) {
        return new GeoEntityNotification(entity, ADD);
    }

    public static GeoEntityNotification createRemove(GeoEntity entity) {
        return new GeoEntityNotification(entity, REMOVE);
    }

    public static GeoEntityNotification createUpdate(GeoEntity entity) {
        return new GeoEntityNotification(entity, UPDATE);
    }

    private GeoEntity mGeoEntity;
    private int mAction;

    private GeoEntityNotification(GeoEntity geoEntity, int action) {
        mGeoEntity = geoEntity;
        mAction = action;
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    public int getAction() {
        return mAction;
    }
}
