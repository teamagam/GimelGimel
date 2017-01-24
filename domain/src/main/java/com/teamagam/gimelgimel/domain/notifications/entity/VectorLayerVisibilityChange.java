package com.teamagam.gimelgimel.domain.notifications.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

public class VectorLayerVisibilityChange {
    private final VectorLayer mVectorLayer;
    private final boolean mVisibility;

    public VectorLayerVisibilityChange(VectorLayer geoEntity, boolean visibility) {
        mVectorLayer = geoEntity;
        mVisibility = visibility;
    }

    public VectorLayer getVectorLayer() {
        return mVectorLayer;
    }

    public boolean getVisibility() {
        return mVisibility;
    }

}
