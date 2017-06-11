package com.teamagam.gimelgimel.data.message.adapters;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;

public class ChatAlert {
  private final ChatMessage mMessage;
  private final Alert mAlert;

  public ChatAlert(ChatMessage message, Alert alert) {
    mMessage = message;
    mAlert = alert;
  }

  public ChatMessage getMessage() {
    return mMessage;
  }

  public Alert getAlert() {
    return mAlert;
  }
}
