package com.teamagam.gimelgimel.domain.alerts.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;

public class GeoAlert extends Alert {

    private final AlertEntity mEntity;

    public GeoAlert(String alertId, int severity, String text, String source,
                    AlertEntity entity) {
        super(alertId, severity, text, source);
        mEntity = entity;
    }

    public AlertEntity getEntity() {
        return mEntity;
    }
}
