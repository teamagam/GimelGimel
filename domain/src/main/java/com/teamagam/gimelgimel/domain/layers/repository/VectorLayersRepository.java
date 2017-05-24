package com.teamagam.gimelgimel.domain.layers.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

public interface VectorLayersRepository {
  void put(VectorLayer vectorLayer);

  VectorLayer get(String id);

  boolean contains(String id);
}
