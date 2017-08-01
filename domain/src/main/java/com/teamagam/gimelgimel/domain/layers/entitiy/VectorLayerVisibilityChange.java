package com.teamagam.gimelgimel.domain.layers.entitiy;

import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityChange;

public class VectorLayerVisibilityChange extends VisibilityChange {

  private final String mVectorLayerId;

  public VectorLayerVisibilityChange(String vectorLayerId, boolean visibility) {
    super(visibility);
    mVectorLayerId = vectorLayerId;
  }

  @Override
  public String getId() {
    return mVectorLayerId;
  }
}
