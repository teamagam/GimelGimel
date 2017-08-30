package com.teamagam.gimelgimel.domain.alerts.entity;

public class Alert {

  private static final String TYPE_BUBBLE = "bubble";

  private final String mAlertId;
  private final String mSource;
  private final Type mType;
  private final int mSeverity;
  private final long mTime;

  public Alert(String alertId, int severity, String source, long time, Type type) {
    mAlertId = alertId;
    mSeverity = severity;
    mSource = source;
    mTime = time;
    mType = type;
  }

  public Alert(String alertId, int severity, String source, long time) {
    this(alertId, severity, source, time, Type.DEFAULT);
  }

  public String getId() {
    return mAlertId;
  }

  public String getSource() {
    return mSource;
  }

  public Type getType() {
    return mType;
  }

  public int getSeverity() {
    return mSeverity;
  }

  public long getTime() {
    return mTime;
  }

  public enum Type {
    DEFAULT,
    VECTOR_LAYER
  }
}
