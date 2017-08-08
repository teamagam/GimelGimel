package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import java.util.List;

@Dao
public interface UserLocationDao {
  @Query("SELECT * FROM user_locations")
  List<UserLocationEntity> getUserLocations();

  @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
  @Query("SELECT *, MAX(time) FROM user_locations GROUP BY user")
  List<UserLocationEntity> getLastLocations();

  @Query("SELECT *, MAX(time) FROM user_locations where time <= :maxTimestamp GROUP BY user")
  List<UserLocationEntity> getLastLocations(long maxTimestamp);

  @Query("SELECT MIN(time) FROM user_locations")
  long getMinimumTimestamp();

  @Query("SELECT MAX(time) FROM user_locations")
  long getMaximumTimestamp();

  @Insert
  void insertUserLocation(UserLocationEntity entity);

  @Query("DELETE FROM user_locations")
  void nukeTable();
}
