package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IFeatureMessageVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class AlertFeature implements IFeatureMessageVisitable {

  private static final String TYPE_BUBBLE = "bubble";

  private final String mAlertId;
  private final String mSource;
  private final String mText;
  private final int mSeverity;
  private final long mTime;

  public AlertFeature(String alertId, int severity, String text, String source, long time) {
    mAlertId = alertId;
    mSeverity = severity;
    mText = text;
    mSource = source;
    mTime = time;
  }

  public String getId() {
    return mAlertId;
  }

  public String getSource() {
    return mSource;
  }

  public String getText() {
    return mText;
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
