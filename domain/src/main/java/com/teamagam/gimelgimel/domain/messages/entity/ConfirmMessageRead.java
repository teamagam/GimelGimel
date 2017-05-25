package com.teamagam.gimelgimel.domain.messages.entity;

public class ConfirmMessageRead {

  private String mSenderId;
  private String mMessageId;

  public ConfirmMessageRead(String senderId, String messageId) {
    mSenderId = senderId;
    mMessageId = messageId;
  }

  public String getSenderId() {
    return mSenderId;
  }

  public void setSenderId(String senderId) {
    mSenderId = senderId;
  }

  public String getMessageId() {
    return mMessageId;
  }

  public void setMessageId(String messageId) {
    mMessageId = messageId;
  }
}
