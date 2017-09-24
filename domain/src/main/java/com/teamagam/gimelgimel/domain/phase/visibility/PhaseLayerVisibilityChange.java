package com.teamagam.gimelgimel.domain.phase.visibility;

import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityChange;

public class PhaseLayerVisibilityChange extends VisibilityChange {

  private final String mPhaseLayerId;

  public PhaseLayerVisibilityChange(boolean visibility, String phaseLayerId) {
    super(visibility);
    mPhaseLayerId = phaseLayerId;
  }

  @Override
  public String getId() {
    return mPhaseLayerId;
  }
}