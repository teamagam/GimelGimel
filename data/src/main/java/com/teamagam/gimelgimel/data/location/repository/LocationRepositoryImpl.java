package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.map.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

public class LocationRepositoryImpl implements LocationRepository {

    private GpsLocationProvider mLocationProvider;

    public LocationRepositoryImpl(GpsLocationProvider provider) {
        mLocationProvider = provider;
    }

    @Override
    public PointGeometry getLocation() {
        LocationSampleEntity lastLocationSample = mLocationProvider.getLastLocationSample();
        return lastLocationSample != null ? lastLocationSample.getLocation() : null;
    }
}
