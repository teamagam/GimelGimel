package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ServerIconEntity;
import java.util.List;

@Dao
public interface IconsDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertServerIcon(ServerIconEntity entity);

  @Query("SELECT * FROM server_icons")
  List<ServerIconEntity> getAllServerIcons();

  @Query("SELECT * FROM server_icons WHERE id = :id")
  ServerIconEntity getServerIconById(String id);

  @Query("DELETE FROM server_icons")
  void nukeTable();
}
