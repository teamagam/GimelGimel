package com.teamagam.gimelgimel.domain.layers.repository;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;

public interface VectorLayersRepository {
  void put(VectorLayer vectorLayerContent);

  VectorLayer get(String id);

  boolean contains(String id);
}
