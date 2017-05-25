package com.teamagam.gimelgimel.domain.rasters.repository;

import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityRepository;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;

public interface IntermediateRasterVisibilityRepository
    extends VisibilityRepository<IntermediateRasterVisibilityChange> {

  String getCurrentlyVisibleName();
}
