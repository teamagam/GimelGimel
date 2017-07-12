package com.teamagam.gimelgimel.app.injectors.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.IconsDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
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
  MessagesDao provideMessagesDao(AppDatabase db) {
    //db.messageDao().nukeTable();
    return db.messageDao();
  }

  @Provides
  @Singleton
  UserLocationDao provideUserLocationsDao(AppDatabase db) {
    //db.userLocationsDao().nukeTable();
    return db.userLocationsDao();
  }

  @Provides
  @Singleton
  VectorLayerDao provideVectorLayerDao(AppDatabase db) {
    //db.vectorLayerDao().nukeTable();
    return db.vectorLayerDao();
  }

  @Provides
  @Singleton
  IconsDao provideIconDao(AppDatabase db) {
    return db.iconsDao();
  }
}
