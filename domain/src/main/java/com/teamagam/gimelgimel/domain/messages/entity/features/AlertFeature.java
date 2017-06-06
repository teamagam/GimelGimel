package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class AlertFeature implements IMessageFeatureVisitable {

  private static final String TYPE_BUBBLE = "bubble";

  private final String mAlertId;
  private final String mSource;
  private final int mSeverity;
  private final long mTime;

  public AlertFeature(String alertId, int severity, String source, long time) {
    mAlertId = alertId;
    mSeverity = severity;
    mSource = source;
    mTime = time;
  }

  public String getId() {
    return mAlertId;
  }

  public String getSource() {
    return mSource;
  }

  public int getSeverity() {
    return mSeverity;
  }

  public long getTime() {
    return mTime;
  }

  public boolean isChatAlert() {
    return TYPE_BUBBLE.equalsIgnoreCase(mSource);
  }

  @Override
  public void accept(IMessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
