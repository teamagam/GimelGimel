package com.teamagam.gimelgimel.app.message.model;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitor;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;

public class MessageAlertApp extends MessageApp<Alert> {

  public MessageAlertApp(Alert alert) {
    super(MessageApp.ALERT);
    mContent = alert;
  }

  @Override
  public void accept(IMessageAppVisitor visitor) {
    visitor.visit(this);
  }
}
