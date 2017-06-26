package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.VectorLayerEntity;
import io.reactivex.Flowable;

@Dao
public interface VectorLayerDao {

  @Query("SELECT * FROM layers")
  Flowable<VectorLayerEntity> getVectorLayers();

  @Query("SELECT * FROM layers WHERE id = :id")
  VectorLayerEntity getVectorLayerById(String id);

  @Insert
  void insertVectorLayer(VectorLayerEntity vectorLayerEntity);
}
