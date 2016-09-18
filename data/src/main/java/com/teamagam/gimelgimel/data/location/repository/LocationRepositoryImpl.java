package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import rx.Observable;

public class LocationRepositoryImpl implements LocationRepository {

    private GpsLocationProvider mLocationProvider;

    public LocationRepositoryImpl(GpsLocationProvider provider) {
        mLocationProvider = provider;
    }

    @Override
    public Observable<PointGeometry> getLocation() {
        Observable<LocationSampleEntity> location = mLocationProvider.getLastLocationSample();

        if (location == null) {
            return Observable.just(null);
        }

        return location.map(LocationSampleEntity::getLocation);
    }
}
