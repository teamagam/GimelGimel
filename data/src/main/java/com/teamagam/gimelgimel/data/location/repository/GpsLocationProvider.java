package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

public interface GpsLocationProvider {

    void start();

    void stop();

    void addListener(GpsLocationListener listener);

    void removeListener(GpsLocationListener listener);

    LocationSampleEntity getLastLocationSample();
}
