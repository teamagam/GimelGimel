package com.teamagam.gimelgimel.data.phases.visibility;

import com.teamagam.gimelgimel.data.base.visibility.VisibilityDataRepository;
import com.teamagam.gimelgimel.domain.phase.visibility.PhaseLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.phase.visibility.PhaseLayerVisibilityRepository;
import javax.inject.Inject;

public class PhaseLayerVisibilityDataRepository
    extends VisibilityDataRepository<PhaseLayerVisibilityChange>
    implements PhaseLayerVisibilityRepository {

  @Inject
  public PhaseLayerVisibilityDataRepository() {
    super();
  }
}