package com.teamagam.gimelgimel.domain.alerts.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

public class VectorLayerAlert extends Alert {

    private static final int VECTOR_LAYER_ALERT_SEVERITY = 1;
    private static final String VECTOR_LAYER_ALERT_SOURCE = "SELF_GENERATED";
    private static final String NO_TEXT_CONTENT = "";
    private final VectorLayer mVectorLayer;

    public VectorLayerAlert(String alertId, long time, VectorLayer vectorLayer) {
        super(
                alertId,
                VECTOR_LAYER_ALERT_SEVERITY,
                NO_TEXT_CONTENT,
                VECTOR_LAYER_ALERT_SOURCE,
                time);
        mVectorLayer = vectorLayer;
    }

    public VectorLayer getVectorLayer() {
        return mVectorLayer;
    }

    @Override
    public boolean isChatAlert() {
        return true;
    }
}
