package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import rx.Observable;

public interface GpsLocationProvider {
    Observable<LocationSampleEntity> getLastLocationSample();
}
