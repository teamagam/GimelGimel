package com.teamagam.gimelgimel.data.message.repository.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.AlertFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.DateConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.FeatureListConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.GeoFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.ImageFeatureEntityConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.PointGeometryConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.Converters.UrlConverter;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessageDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;

@Database(entities = {
    ChatMessageEntity.class, UserLocationEntity.class, VectorLayerEntity.class
}, version = 1, exportSchema = false)
@TypeConverters({
    DateConverter.class, PointGeometryConverter.class, UrlConverter.class,
    GeoFeatureEntityConverter.class, ImageFeatureEntityConverter.class,
    AlertFeatureEntityConverter.class, FeatureListConverter.class
})
public abstract class AppDatabase extends RoomDatabase {
  public abstract MessageDao messageDao();

  public abstract UserLocationDao userLocationDao();

  public abstract VectorLayerDao vectorLayerDao();
}
