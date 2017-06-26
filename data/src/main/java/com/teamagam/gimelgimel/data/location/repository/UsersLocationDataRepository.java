package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.data.message.repository.cache.room.UserLocationsEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UsersLocationDataRepository implements UsersLocationRepository {

  private UserLocationDao mUserLocationDao;
  private UserLocationsEntityMapper mMapper;

  @Inject
  public UsersLocationDataRepository(UserLocationDao userLocationDao,
      UserLocationsEntityMapper mapper) {
    mUserLocationDao = userLocationDao;
    mMapper = mapper;
  }

  @Override
  public void add(UserLocation userLocation) {
    mUserLocationDao.insertUserLocation(mMapper.convertToEntity(userLocation));
  }

  @Override
  public Iterable<UserLocation> getLastLocations() {
    List<UserLocationEntity> lastLocations = mUserLocationDao.getLastLocations();
    List<UserLocation> userLocations = new ArrayList<>();

    for (UserLocationEntity entity : lastLocations) {
      userLocations.add(mMapper.convertToDomain(entity));
    }

    return userLocations;
  }
}
