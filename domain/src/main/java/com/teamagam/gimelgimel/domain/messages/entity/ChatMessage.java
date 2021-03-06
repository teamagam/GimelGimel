package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import java.util.Date;

public class ChatMessage extends OutGoingChatMessage {

  private String mMessageId;
  private Date mCreatedAt;

  public ChatMessage(String messageId,
      String senderId,
      Date createdAt,
      MessageFeatureVisitable... features) {
    super(senderId, features);
    mMessageId = messageId;
    mCreatedAt = createdAt;
  }

  public String getMessageId() {
    return mMessageId;
  }

  public Date getCreatedAt() {
    return mCreatedAt;
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