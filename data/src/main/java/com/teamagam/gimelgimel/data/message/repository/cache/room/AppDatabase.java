package com.teamagam.gimelgimel.data.message.repository.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.teamagam.gimelgimel.data.dynamicLayers.room.converter.DynamicEntityArrayConverter;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicEntityDbEntity;
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
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.SearchMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ServerIconEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;
import com.teamagam.gimelgimel.data.phases.repository.room.DynamicLayerArrayConverter;
import com.teamagam.gimelgimel.data.phases.repository.room.PhaseLayerDao;
import com.teamagam.gimelgimel.data.phases.repository.room.PhaseLayerEntity;

@Database(entities = {
    OutGoingChatMessageEntity.class, ChatMessageEntity.class, UserLocationEntity.class,
    DynamicLayerEntity.class, DynamicEntityDbEntity.class, PhaseLayerEntity.class,
    VectorLayerEntity.class, ServerIconEntity.class, AlertedVectorLayerEntity.class
}, version = 1, exportSchema = false)
@TypeConverters({
    DateConverter.class, PointConverter.class, UrlConverter.class, GeoFeatureEntityConverter.class,
    DynamicEntityArrayConverter.class, ImageFeatureEntityConverter.class,
    AlertFeatureEntityConverter.class, FeatureListConverter.class, DynamicLayerArrayConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
  public abstract SearchMessagesDao searchMessagesDao();

  public abstract MessagesDao messageDao();

  public abstract UserLocationDao userLocationDao();

  public abstract OutGoingMessagesDao outgoingMessageDao();

  public abstract VectorLayerDao vectorLayerDao();

  public abstract DynamicLayerDao dynamicLayerDao();

  public abstract PhaseLayerDao phaseLayerDao();

  public abstract IconsDao iconsDao();

  public abstract AlertedVectorLayerDao alertedVectorLayerDao();
}
