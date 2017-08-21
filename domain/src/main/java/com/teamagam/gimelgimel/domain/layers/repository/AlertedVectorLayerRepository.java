package com.teamagam.gimelgimel.domain.layers.repository;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;

public interface AlertedVectorLayerRepository {

  void markAsAlerted(VectorLayer vectorLayer);

  boolean isAlerted(VectorLayer vectorLayer);
}