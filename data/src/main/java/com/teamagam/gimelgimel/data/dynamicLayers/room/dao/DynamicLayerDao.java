package com.teamagam.gimelgimel.data.dynamicLayers.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import java.util.List;

@Dao
public interface DynamicLayerDao {

  @Query("SELECT *,MAX(timestamp) FROM dynamic_layers GROUP BY id")
  List<DynamicLayerEntity> getLatestDynamicLayers();

  @Query("SELECT *, MAX(timestamp) FROM dynamic_layers WHERE id = :id GROUP BY id")
  DynamicLayerEntity getLatestDynamicLayerById(String id);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertDynamicLayer(DynamicLayerEntity dynamicLayerEntity);

  @Query("DELETE FROM dynamic_layers")
  void nukeTable();
}
