package com.teamagam.gimelgimel.domain.notifications.entity;

public class VectorLayerVisibilityChange {
    private final String mVectorLayerId;
    private final boolean mIsVisible;

    public VectorLayerVisibilityChange(String vectorLayerId, boolean visibility) {
        mVectorLayerId = vectorLayerId;
        mIsVisible = visibility;
    }

    public String getVectorLayerId() {
        return mVectorLayerId;
    }

    public boolean getVisibility() {
        return mIsVisible;
    }

}
