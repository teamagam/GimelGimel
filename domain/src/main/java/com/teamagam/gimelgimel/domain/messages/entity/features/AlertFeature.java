package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;

public class AlertFeature implements MessageFeatureVisitable {

  private final Alert mAlert;

  public AlertFeature(Alert alert) {
    mAlert = alert;
  }

  public Alert getAlert() {
    return mAlert;
  }

  @Override
  public void accept(MessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
