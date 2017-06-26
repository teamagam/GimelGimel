package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import io.reactivex.Flowable;
import java.util.List;

@Dao
public interface UserLocationDao {
  @Query("SELECT * FROM user_locations")
  Flowable<UserLocationEntity> getUserLocations();

  @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
  @Query("SELECT *, MAX(time) FROM user_locations GROUP BY user")
  List<UserLocationEntity> getLastLocations();

  @Insert
  void insertUserLocation(UserLocationEntity entity);
}
