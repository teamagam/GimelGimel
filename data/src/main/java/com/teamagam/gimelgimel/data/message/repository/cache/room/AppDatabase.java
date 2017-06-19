package com.teamagam.gimelgimel.data.message.repository.cache.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.teamagam.gimelgimel.data.response.entity.SuperTest;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.ResponseDao;

@Database(entities = { SuperTest.class }, version = 1, exportSchema = false)
@TypeConverters({ Converter.class })
public abstract class AppDatabase extends RoomDatabase {
  public abstract ResponseDao responseDao();
}
