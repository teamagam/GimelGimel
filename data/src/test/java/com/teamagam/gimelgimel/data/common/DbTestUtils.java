package com.teamagam.gimelgimel.data.common;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;

public class DbTestUtils {
  public static AppDatabase getDB(Context context) {
    return Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
        .allowMainThreadQueries()
        .build();
  }
}
