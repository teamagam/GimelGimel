package com.teamagam.gimelgimel.app.model.entities;

/**
 * Encapsulates a location data associated with a user
 */
public class UserLocation {
    private final String mId;
    private final LocationSample mLocationSample;

    public UserLocation(String id, LocationSample locationSample) {
        mId = id;
        mLocationSample = locationSample;
    }

    public String getId() {
        return mId;
    }

    public LocationSample getLocationSample() {
        return mLocationSample;
    }

    public long getAgeMillis() {
        return mLocationSample.getAgeMillis();
    }
}
