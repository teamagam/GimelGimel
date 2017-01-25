package com.teamagam.gimelgimel.domain.alerts.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;

public class Alert {

    public static String TYPE_BUBBLE = "bubble";
    public static String TYPE_SENSOR = "sensor";

    private final String mSource;
    private final long mTime;
    private final String mText;
    private final int mSeverity;
    private final String mMessageId;
    private final AlertEntity mEntity;

    public Alert(String source, long time, String text, int severity, String messageId, AlertEntity entity) {
        mSource = source;
        mTime = time;
        mText = text;
        mSeverity = severity;
        mMessageId = messageId;
        mEntity = entity;
    }

    public AlertEntity getEntity() {
        return mEntity;
    }

    public String getSource() {
        return mSource;
    }

    public long getTime() {
        return mTime;
    }

    public String getText() {
        return mText;
    }

    public int getSeverity() {
        return mSeverity;
    }

    public String getMessageId() {
        return mMessageId;
    }
}
