package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.base.visibility.VisibilityDataRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorLayersVisibilityDataRepository
    extends VisibilityDataRepository<VectorLayerVisibilityChange>
    implements VectorLayersVisibilityRepository {

  @Inject
  public VectorLayersVisibilityDataRepository() {
    super();
  }
}
