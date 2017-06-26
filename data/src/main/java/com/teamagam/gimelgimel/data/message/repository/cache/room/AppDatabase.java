package com.teamagam.gimelgimel.data.message.repository.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.AlertFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.DateConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.FeatureListConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.GeoFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.ImageFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.PointConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.converters.UrlConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;

@Database(entities = {
    ChatMessageEntity.class, UserLocationEntity.class, VectorLayerEntity.class
}, version = 1, exportSchema = false)
@TypeConverters({
    DateConverter.class, PointConverter.class, UrlConverter.class,
    GeoFeatureEntityConverter.class, ImageFeatureEntityConverter.class,
    AlertFeatureEntityConverter.class, FeatureListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
  public abstract MessagesDao messageDao();

  public abstract UserLocationDao userLocationsDao();

  public abstract VectorLayerDao vectorLayerDao();
}
