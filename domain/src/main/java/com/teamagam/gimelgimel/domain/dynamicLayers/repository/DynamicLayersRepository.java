package com.teamagam.gimelgimel.domain.dynamicLayers.repository;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

public interface DynamicLayersRepository {
  void put(DynamicLayer dynamicLayer);

  DynamicLayer getById(String id);

  boolean contains(String id);
}
