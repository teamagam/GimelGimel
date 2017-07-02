package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.UserLocationsEntityMapper;
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

  private UserLocationDao mDao;
  private UserLocationsEntityMapper mMapper;

  @Inject
  public UsersLocationDataRepository(UserLocationDao dao,
      UserLocationsEntityMapper mapper) {
    mDao = dao;
    mMapper = mapper;
  }

  @Override
  public void add(UserLocation userLocation) {
    mDao.insertUserLocation(mMapper.mapToEntity(userLocation));
  }

  @Override
  public Iterable<UserLocation> getLastLocations() {
    List<UserLocationEntity> lastLocationsEntities = mDao.getLastLocations();
    List<UserLocation> userLocations = new ArrayList<>();

    for (UserLocationEntity entity : lastLocationsEntities) {
      userLocations.add(mMapper.mapToDomain(entity));
    }

    return userLocations;
  }
}
