package com.teamagam.gimelgimel.domain.base.visiblity;

public class VisibilityChange {

    private final boolean mIsVisible;

    public VisibilityChange(boolean visibility) {
        mIsVisible = visibility;
    }

    public boolean isVisible() {
        return mIsVisible;
    }
}
