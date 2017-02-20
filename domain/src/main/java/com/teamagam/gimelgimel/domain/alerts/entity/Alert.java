package com.teamagam.gimelgimel.domain.alerts.entity;

import java.util.Date;

public class Alert {

    private static final String TYPE_BUBBLE = "bubble";

    private final String mSource;
    private final long mTime;
    private final String mText;
    private final int mSeverity;
    private final String mMessageId;

    public Alert(String messageId, int severity, String text, long time, String source) {
        mMessageId = messageId;
        mSeverity = severity;
        mText = text;
        mTime = time;
        mSource = source;
    }

    public String getSource() {
        return mSource;
    }

    public Date getDate() {
        return new Date(mTime);
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

    public boolean isBubbleAlert() {
        return TYPE_BUBBLE.equalsIgnoreCase(mSource);
    }
}
