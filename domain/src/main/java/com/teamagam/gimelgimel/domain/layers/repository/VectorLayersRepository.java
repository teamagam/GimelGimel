package com.teamagam.gimelgimel.domain.layers.repository;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import io.reactivex.Observable;

public interface VectorLayersRepository {
  Observable<VectorLayer> getVectorLayersObservable();

  void put(VectorLayer vectorLayer);

  VectorLayer get(String id);

  boolean contains(String id);

  boolean isOutdatedVectorLayer(VectorLayer vectorLayer);
}
