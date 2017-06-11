package com.teamagam.gimelgimel.domain.location.respository;

import com.teamagam.gimelgimel.domain.location.entity.UserLocation;

public interface UsersLocationRepository {

  void add(UserLocation userLocation);

  Iterable<UserLocation> getLastLocations();
}
