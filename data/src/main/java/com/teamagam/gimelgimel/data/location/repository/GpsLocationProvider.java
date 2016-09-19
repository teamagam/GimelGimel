package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

public interface GpsLocationProvider {
    LocationSampleEntity getLastLocationSample();
}
