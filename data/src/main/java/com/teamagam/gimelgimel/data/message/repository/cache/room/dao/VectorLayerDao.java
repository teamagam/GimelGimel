package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;
import io.reactivex.Flowable;
import java.util.List;

@Dao
public interface VectorLayerDao {

  @Query("SELECT * FROM layers")
  List<VectorLayerEntity> getAllVectorLayers();

  @SuppressWarnings((RoomWarnings.CURSOR_MISMATCH))
  @Query("SELECT MAX(rowid), * FROM layers")
  Flowable<VectorLayerEntity> getLatestVectorLayer();

  @Query("SELECT * FROM layers WHERE id = :id")
  VectorLayerEntity getVectorLayerById(String id);

  @Insert
  void insertVectorLayer(VectorLayerEntity vectorLayerEntity);

  @Query("DELETE FROM layers")
  void nukeTable();
}
