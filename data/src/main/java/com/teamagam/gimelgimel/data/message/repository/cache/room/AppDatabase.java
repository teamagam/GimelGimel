package com.teamagam.gimelgimel.data.message.repository.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.teamagam.gimelgimel.data.dynamicLayers.room.converter.GeoFeatureEntityArrayConverter;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.layers.AlertedVectorLayerDao;
import com.teamagam.gimelgimel.data.layers.AlertedVectorLayerEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.AlertFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.DateConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.FeatureListConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.GeoFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.ImageFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.PointConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.UrlConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.IconsDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ServerIconEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;

@Database(entities = {
    OutGoingChatMessageEntity.class, ChatMessageEntity.class, UserLocationEntity.class,
    DynamicLayerEntity.class, VectorLayerEntity.class, ServerIconEntity.class,
    AlertedVectorLayerEntity.class
}, version = 1, exportSchema = false)
@TypeConverters({
    DateConverter.class, PointConverter.class, UrlConverter.class, GeoFeatureEntityConverter.class,
    GeoFeatureEntityArrayConverter.class, ImageFeatureEntityConverter.class,
    AlertFeatureEntityConverter.class, FeatureListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
  public abstract MessagesDao messageDao();

  public abstract UserLocationDao userLocationDao();

  public abstract OutGoingMessagesDao outgoingMessageDao();

  public abstract VectorLayerDao vectorLayerDao();

  public abstract DynamicLayerDao dynamicLayerDao();

  public abstract IconsDao iconsDao();

  public abstract AlertedVectorLayerDao alertedVectorLayerDao();
}
