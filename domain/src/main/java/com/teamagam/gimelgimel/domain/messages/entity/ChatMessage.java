package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import java.util.Date;

public class ChatMessage extends OutGoingChatMessage {

  private String mMessageId;

  public ChatMessage(String messageId,
      String senderId,
      Date createdAt,
      MessageFeatureVisitable... features) {
    super(senderId, createdAt, features);
    mMessageId = messageId;
  }

  public ChatMessage(OutGoingChatMessage outGoingChatMessage, String messageId) {
    super(outGoingChatMessage.getSenderId(), outGoingChatMessage.getCreatedAt(),
        outGoingChatMessage.getFeatures());
    mMessageId = messageId;
  }

  public String getMessageId() {
    return mMessageId;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ChatMessage) {
      return mMessageId.equals(((ChatMessage) o).getMessageId());
    } else {
      return super.equals(o);
    }
  }
}