package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

public class DynamicLayerPresentation extends DynamicLayer {

  private boolean mIsShown;

  public DynamicLayerPresentation(DynamicLayer dl, boolean isShown) {
    super(dl.getId(), dl.getName(), dl.getEntities());
    mIsShown = isShown;
  }

  public boolean isShown() {
    return mIsShown;
  }
}
