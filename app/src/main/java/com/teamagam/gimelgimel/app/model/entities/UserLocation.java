package com.teamagam.gimelgimel.app.model.entities;

public class UserLocation {
    private final String id;
    private final LocationSample locationSample;

    public UserLocation(String id, LocationSample locationSample) {
        this.id = id;
        this.locationSample = locationSample;
    }

    public String getId() {
        return id;
    }

    public LocationSample getLocationSample() {
        return locationSample;
    }
}
