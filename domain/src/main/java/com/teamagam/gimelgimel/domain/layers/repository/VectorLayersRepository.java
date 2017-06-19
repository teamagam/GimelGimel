package com.teamagam.gimelgimel.domain.layers.repository;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import io.reactivex.Observable;

public interface VectorLayersRepository {
  Observable<VectorLayer> getVectorLayersObservable();

  void put(VectorLayer vectorLayerContent);

  VectorLayer get(String id);

  boolean contains(String id);

  boolean isOutdatedVectorLayer(VectorLayer vectorLayer);
}
