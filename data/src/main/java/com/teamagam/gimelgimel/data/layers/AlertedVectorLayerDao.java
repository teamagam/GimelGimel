package com.teamagam.gimelgimel.data.layers;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface AlertedVectorLayerDao {

  @Query("SELECT 1 FROM alerted_vls WHERE id = :id AND version = :version")
  boolean contains(String id, int version);

  @Insert
  void insert(AlertedVectorLayerEntity alertedVectorLayerEntity);

  @Query("DELETE FROM alerted_vls")
  void nukeTable();
}