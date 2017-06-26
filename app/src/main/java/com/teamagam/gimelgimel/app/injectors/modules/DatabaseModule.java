package com.teamagam.gimelgimel.app.injectors.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
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
    return db.messageDao();
  }
}
