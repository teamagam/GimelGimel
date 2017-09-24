package com.teamagam.gimelgimel.data.phases.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import java.util.List;

@Dao
public interface PhaseLayerDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(PhaseLayerEntity entity);

  @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
  @Query("SELECT *, MAX(timestamp) FROM phase_layers WHERE id = :id GROUP BY id")
  PhaseLayerEntity getLatestPhaseLayerById(String id);

  @Query("SELECT 1 FROM phase_layers WHERE id = :id")
  boolean contains(String id);

  @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
  @Query("SELECT *,MAX(timestamp) FROM phase_layers GROUP BY id")
  List<PhaseLayerEntity> getLatestPhaseLayers();

  @Query("DELETE FROM phase_layers")
  void nukeTable();
}
