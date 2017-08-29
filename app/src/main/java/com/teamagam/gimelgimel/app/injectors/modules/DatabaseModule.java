package com.teamagam.gimelgimel.app.injectors.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.layers.AlertedVectorLayerDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.IconsDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.OutGoingMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.SearchMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Singleton
@Module
public class DatabaseModule {

  @Provides
  @Singleton
  AppDatabase provideAppDatabase(Context context) {
    return Room.databaseBuilder(context, AppDatabase.class, Constants.DATABASE_NAME)
        .allowMainThreadQueries()
        .build();
  }

  @Provides
  @Singleton
  MessagesDao provideMessageDao(AppDatabase db) {
    return db.messageDao();
  }

  @Provides
  @Singleton
  UserLocationDao provideUserLocationDao(AppDatabase db) {
    return db.userLocationDao();
  }

  @Provides
  @Singleton
  OutGoingMessagesDao provideOutGoingMessagesDao(AppDatabase db) {
    return db.outgoingMessageDao();
  }

  @Provides
  @Singleton
  VectorLayerDao provideVectorLayerDao(AppDatabase db) {
    return db.vectorLayerDao();
  }

  @Provides
  @Singleton
  DynamicLayerDao provideDynamicLayerDao(AppDatabase db) {
    return db.dynamicLayerDao();
  }

  @Provides
  @Singleton
  IconsDao provideIconDao(AppDatabase db) {
    return db.iconsDao();
  }

  @Provides
  @Singleton
  AlertedVectorLayerDao provideAlertedVectorLayerDao(AppDatabase db) {
    return db.alertedVectorLayerDao();
  }

  @Provides
  @Singleton
  SearchMessagesDao provideSearchMessagesDao(AppDatabase db) {
    return db.searchMessagesDao();
  }
}
