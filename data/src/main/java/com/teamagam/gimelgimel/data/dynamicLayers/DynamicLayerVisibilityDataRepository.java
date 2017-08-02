package com.teamagam.gimelgimel.data.dynamicLayers;

import com.teamagam.gimelgimel.data.base.visibility.VisibilityDataRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import javax.inject.Inject;

public class DynamicLayerVisibilityDataRepository
    extends VisibilityDataRepository<DynamicLayerVisibilityChange>
    implements DynamicLayerVisibilityRepository {

  @Inject
  public DynamicLayerVisibilityDataRepository() {
    super();
  }
}
