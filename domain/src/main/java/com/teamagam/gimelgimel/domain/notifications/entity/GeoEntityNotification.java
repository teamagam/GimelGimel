package com.teamagam.gimelgimel.domain.notifications.entity;

import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;

/**
 * Notification for syncing map with Displayed repository
 */

public class GeoEntityNotification {

    public final static int ADD = 1;
    public final static int REMOVE = 2;
    public final static int UPDATE = 3;

    private GeoEntity mGeoEntity;

    private String mVectorLayerId;

    private int mAction;
    public GeoEntityNotification(GeoEntity geoEntity, String vectorLayerId, int action) {
        mGeoEntity = geoEntity;
        mVectorLayerId = vectorLayerId;
        mAction = action;
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    public String getVectorLayerId() {
        return mVectorLayerId;
    }

    public int getAction() {
        return mAction;
    }

}
