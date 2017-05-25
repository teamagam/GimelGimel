package com.teamagam.gimelgimel.data.message.entity;

import com.google.gson.annotations.SerializedName;

public class ConfirmMessageReadData {

  @SerializedName("senderId")
  private String mSenderId;
  @SerializedName("messageId")
  private String mMessageId;

  public ConfirmMessageReadData(String senderId, String messageId) {
    mSenderId = senderId;
    mMessageId = messageId;
  }

  public ConfirmMessageReadData() {
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
