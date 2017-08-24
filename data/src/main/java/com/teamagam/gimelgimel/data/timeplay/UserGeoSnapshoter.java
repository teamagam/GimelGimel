package com.teamagam.gimelgimel.data.timeplay;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.UserLocationsEntityMapper;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.List;
import javax.inject.Inject;

public class UserGeoSnapshoter implements GeoSnapshoter {

  private final UserLocationDao mUserLocationDao;
  private final UserLocationsEntityMapper mUserLocationsEntityMapper;

  @Inject
  public UserGeoSnapshoter(UserLocationDao userLocationDao,
      UserLocationsEntityMapper userLocationsEntityMapper) {
    mUserLocationDao = userLocationDao;
    mUserLocationsEntityMapper = userLocationsEntityMapper;
  }

  @Override
  public List<GeoEntity> snapshot(long maxTimestamp) {
    return Lists.transform(getUserEntities(maxTimestamp), x -> x);
  }

  private List<UserEntity> getUserEntities(long timestamp) {
    List<UserLocationEntity> lastLocations = mUserLocationDao.getLastLocations(timestamp);
    List<UserLocation> userLocations =
        Lists.transform(lastLocations, mUserLocationsEntityMapper::mapToDomain);
    return Lists.transform(userLocations, UserLocation::createUserEntity);
  }
}
