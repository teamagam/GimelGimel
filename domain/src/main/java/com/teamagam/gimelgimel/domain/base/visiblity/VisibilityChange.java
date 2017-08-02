package com.teamagam.gimelgimel.domain.base.visiblity;

public abstract class VisibilityChange {

  private final boolean mIsVisible;

  public VisibilityChange(boolean visibility) {
    mIsVisible = visibility;
  }

  public boolean isVisible() {
    return mIsVisible;
  }

  public abstract String getId();
}
