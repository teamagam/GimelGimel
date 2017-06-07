package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import java.util.Date;

public class MessageAlert extends Message {

  private final Alert mAlert;

  public MessageAlert(String messageId, String senderId, Date createdAt, Alert alert) {
    super(messageId, senderId, createdAt);
    mAlert = alert;
  }

  public Alert getAlert() {
    return mAlert;
  }
}
