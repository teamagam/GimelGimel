package com.teamagam.gimelgimel.domain.rasters.entity;

import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityChange;

public class IntermediateRasterVisibilityChange extends VisibilityChange {

  private final String mIntermediateRasterName;

  public IntermediateRasterVisibilityChange(boolean visibility, String intermediateRasterName) {
    super(visibility);
    mIntermediateRasterName = intermediateRasterName;
  }

  public String getIntermediateRasterName() {
    return mIntermediateRasterName;
  }
}
