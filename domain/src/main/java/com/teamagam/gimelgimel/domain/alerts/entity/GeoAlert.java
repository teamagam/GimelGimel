package com.teamagam.gimelgimel.domain.alerts.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;

public class GeoAlert extends Alert {

    private final AlertEntity mEntity;

    public GeoAlert(String source, long time, String text, int severity, String messageId,
                    AlertEntity entity) {
        super(messageId, severity, text, time, source);
        mEntity = entity;
    }

    public AlertEntity getEntity() {
        return mEntity;
    }
}
