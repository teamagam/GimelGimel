package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class AlertFeature implements IMessageFeatureVisitable {

  private final String mAlertId;

  public AlertFeature(String alertId) {
    mAlertId = alertId;
  }

  public String getId() {
    return mAlertId;
  }

  @Override
  public void accept(IMessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
