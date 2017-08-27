package com.teamagam.gimelgimel.data.timeplay;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class GeoMessagesSnapshoter implements GeoSnapshoter {

  private final MessagesDao mMessagesDao;
  private final GeoFeatureEntityMapper mGeoFeatureEntityMapper;

  @Inject
  public GeoMessagesSnapshoter(MessagesDao messagesDao,
      GeoFeatureEntityMapper geoFeatureEntityMapper) {
    mMessagesDao = messagesDao;
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
  }

  @Override
  public List<GeoEntity> snapshot(long maxTimestamp) {
    return getGeoMessageEntities(maxTimestamp);
  }

  private List<GeoEntity> getGeoMessageEntities(long timestamp) {
    List<ChatMessageEntity> geoMessages = mMessagesDao.getGeoMessages(new Date(timestamp));
    List<GeoFeatureEntity> geoFeatures =
        Lists.transform(geoMessages, chatMessageEntity -> chatMessageEntity.geoFeatureEntity);
    return Lists.transform(geoFeatures, mGeoFeatureEntityMapper::mapToDomain);
  }
}
