package com.teamagam.gimelgimel.domain.alerts.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;

public class GeoAlert extends Alert {

    private final AlertEntity mEntity;

    public GeoAlert(String messageId, int severity, String text, long time, String source,
                    AlertEntity entity) {
        super(messageId, severity, text, time, source);
        mEntity = entity;
    }

    public AlertEntity getEntity() {
        return mEntity;
    }
}
