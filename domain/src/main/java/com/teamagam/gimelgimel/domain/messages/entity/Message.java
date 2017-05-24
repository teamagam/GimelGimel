package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitable;
import java.util.Date;

public abstract class Message implements IMessageVisitable {

  private String mMessageId;
  private String mSenderId;
  private Date mCreatedAt;

  public Message(String messageId, String senderId, Date createdAt) {
    mMessageId = messageId;
    mSenderId = senderId;
    mCreatedAt = createdAt;
  }

  public String getMessageId() {
    return mMessageId;
  }

  public String getSenderId() {
    return mSenderId;
  }

  public Date getCreatedAt() {
    return mCreatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Message) {
      return mMessageId.equals(((Message) o).getMessageId());
    } else {
      return super.equals(o);
    }
  }
}
