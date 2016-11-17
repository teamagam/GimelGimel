package com.teamagam.gimelgimel.domain.location.respository;


import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

public interface UsersLocationRepository {

    void add(String userId, LocationSample lastUserLocation);

}
