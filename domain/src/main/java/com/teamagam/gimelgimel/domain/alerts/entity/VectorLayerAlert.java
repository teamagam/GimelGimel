package com.teamagam.gimelgimel.domain.alerts.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.util.Date;

public class VectorLayerAlert extends Alert {

    private static final int VECTOR_LAYER_ALERT_SEVERITY = 1;
    private static final String VECTOR_LAYER_ALERT_SOURCE = "SELF_GENERATED";
    private final VectorLayer mVectorLayer;

    public VectorLayerAlert(String messageId, VectorLayer vectorLayer) {
        super(messageId, VECTOR_LAYER_ALERT_SEVERITY, "", new Date().getTime(),
                VECTOR_LAYER_ALERT_SOURCE);
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
