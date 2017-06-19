package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.response.entity.SuperTest;
import java.util.List;

@Dao
public interface ResponseDao {

  @Query("SELECT * FROM SuperTest")
  List<SuperTest> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertServerResponse(SuperTest... responses);
}
