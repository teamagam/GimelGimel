package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityChange;

public class DynamicLayerVisibilityChange extends VisibilityChange {

  private final String mId;

  public DynamicLayerVisibilityChange(boolean visibility, String id) {
    super(visibility);
    mId = id;
  }

  @Override
  public String getId() {
    return mId;
  }
}
