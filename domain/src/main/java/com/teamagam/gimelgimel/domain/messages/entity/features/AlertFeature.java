package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class AlertFeature implements IMessageFeatureVisitable {

  private final Alert mAlert;

  public AlertFeature(Alert alert) {
    mAlert = alert;
  }

  public Alert getAlert() {
    return mAlert;
  }

  @Override
  public void accept(IMessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
