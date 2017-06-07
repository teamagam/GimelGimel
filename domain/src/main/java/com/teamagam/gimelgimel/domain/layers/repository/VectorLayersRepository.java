package com.teamagam.gimelgimel.domain.layers.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayerContent;

public interface VectorLayersRepository {
  void put(VectorLayerContent vectorLayerContent);

  VectorLayerContent get(String id);

  boolean contains(String id);
}
