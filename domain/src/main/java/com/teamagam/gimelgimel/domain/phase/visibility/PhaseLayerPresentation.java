package com.teamagam.gimelgimel.domain.phase.visibility;

import com.teamagam.gimelgimel.domain.phase.PhaseLayer;

public class PhaseLayerPresentation extends PhaseLayer {

  private final boolean mIsShown;

  public PhaseLayerPresentation(PhaseLayer layer, boolean isShown) {
    super(layer.getId(), layer.getName(), layer.getDescription(), layer.getTimestamp(),
        layer.getPhases());
    mIsShown = isShown;
  }

  public boolean isShown() {
    return mIsShown;
  }
}