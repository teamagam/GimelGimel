package com.teamagam.gimelgimel.data.timeplay;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.UserLocationsEntityMapper;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class DataGeoSnapshoter implements GeoSnapshoter {

  private final UserLocationDao mUserLocationDao;
  private final MessagesDao mMessagesDao;
  private final GeoFeatureEntityMapper mGeoFeatureEntityMapper;
  private final UserLocationsEntityMapper mUserLocationsEntityMapper;

  @Inject
  public DataGeoSnapshoter(UserLocationDao userLocationDao,
      MessagesDao messagesDao,
      GeoFeatureEntityMapper geoFeatureEntityMapper,
      UserLocationsEntityMapper userLocationsEntityMapper) {
    mUserLocationDao = userLocationDao;
    mMessagesDao = messagesDao;
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
    mUserLocationsEntityMapper = userLocationsEntityMapper;
  }

  @Override
  public List<GeoEntity> snapshot(long maxTimestamp) {
    List<UserEntity> userGeoEntities = getUserEntities(maxTimestamp);
    List<GeoEntity> geoEntities = geoGeoMessageEntities(maxTimestamp);

    List<GeoEntity> res = new ArrayList<>(userGeoEntities);
    res.addAll(geoEntities);
    return res;
  }

  private List<UserEntity> getUserEntities(long timestamp) {
    List<UserLocationEntity> lastLocations = mUserLocationDao.getLastLocations(timestamp);
    List<UserLocation> userLocations =
        Lists.transform(lastLocations, mUserLocationsEntityMapper::mapToDomain);
    return Lists.transform(userLocations, UserLocation::createUserEntity);
  }

  private List<GeoEntity> geoGeoMessageEntities(long timestamp) {
    List<ChatMessageEntity> geoMessages = mMessagesDao.getGeoMessages(new Date(timestamp));
    List<GeoFeatureEntity> geoFeatures = Lists.transform(geoMessages,
        chatMessageEntity -> chatMessageEntity != null ? chatMessageEntity.geoFeatureEntity : null);
    return Lists.transform(geoFeatures, mGeoFeatureEntityMapper::mapToDomain);
  }
}
