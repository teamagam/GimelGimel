package com.teamagam.gimelgimel.domain.dynamicLayers.repository;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import io.reactivex.Observable;

public interface DynamicLayersRepository {
  void put(DynamicLayer dynamicLayer);

  DynamicLayer getById(String id);

  boolean contains(String id);

  Observable<DynamicLayer> getObservable();
}
