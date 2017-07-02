package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.LocationSampleEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserLocationsEntityMapper implements EntityMapper<UserLocation,UserLocationEntity> {

  private GeometryDataMapper mGeometryDataMapper;

  @Inject
  public UserLocationsEntityMapper(GeometryDataMapper geometryDataMapper) {
    mGeometryDataMapper = geometryDataMapper;
  }

  public UserLocation mapToDomain(UserLocationEntity entity) {
    LocationSampleEntity sampleEntity = entity.location;
    return new UserLocation(entity.user, createLocationSample(sampleEntity));
  }

  public UserLocationEntity mapToEntity(UserLocation userLocation) {
    LocationSample sample = userLocation.getLocationSample();
    UserLocationEntity entity = new UserLocationEntity();

    entity.id = UUID.randomUUID().toString();
    entity.user = userLocation.getUser();
    entity.location = new LocationSampleEntity();
    entity.location.point = mGeometryDataMapper.transformToData(sample.getLocation());
    entity.location.time = sample.getTime();
    entity.location.provider = sample.getProvider();
    entity.location.hasSpeed = sample.hasSpeed();
    entity.location.speed = sample.getSpeed();
    entity.location.hasAccuracy = sample.hasAccuracy();
    entity.location.accuracy = sample.getAccuracy();

    return entity;
  }

  private LocationSample createLocationSample(LocationSampleEntity sampleEntity) {
    return new LocationSample(mGeometryDataMapper.transform(sampleEntity.point), sampleEntity.time,
        sampleEntity.provider, sampleEntity.hasSpeed, sampleEntity.speed,
        sampleEntity.hasBearing, sampleEntity.bearing, sampleEntity.hasAccuracy,
        sampleEntity.accuracy);
  }
}
