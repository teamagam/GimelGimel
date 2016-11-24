package com.teamagam.gimelgimel.domain.location.respository;


import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import rx.Observable;

public interface UsersLocationRepository {

    void add(String userId, LocationSample lastUserLocation);

    Observable<UserLocation> getLastLocations();

    Observable<UserLocation> getUsersLocationUpdates();
}
