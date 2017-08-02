package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityChange;

public class DynamicLayerVisibilityChange extends VisibilityChange {

  private final String mDynamicLayerId;

  public DynamicLayerVisibilityChange(boolean visibility, String dynamicLayerId) {
    super(visibility);
    mDynamicLayerId = dynamicLayerId;
  }

  @Override
  public String getId() {
    return mDynamicLayerId;
  }
}
