package com.teamagam.gimelgimel.data.dynamicLayers.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import io.reactivex.Flowable;
import java.util.List;

@Dao
public interface DynamicLayerDao {

  @Query("SELECT * FROM dynamic_layers")
  List<DynamicLayerEntity> getAllDynamicLayers();

  @SuppressWarnings((RoomWarnings.CURSOR_MISMATCH))
  @Query("SELECT *, MAX(rowid) FROM dynamic_layers")
  Flowable<DynamicLayerEntity> getLatestDynamicLayer();

  @Query("SELECT * FROM dynamic_layers WHERE id = :id")
  DynamicLayerEntity getDynamicLayerById(String id);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertDynamicLayer(DynamicLayerEntity dynamicLayerEntity);

  @Query("DELETE FROM dynamic_layers")
  void nukeTable();
}
