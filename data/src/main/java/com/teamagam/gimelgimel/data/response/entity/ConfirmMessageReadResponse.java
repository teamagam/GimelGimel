package com.teamagam.gimelgimel.data.response.entity;

import com.google.gson.annotations.SerializedName;

public class ConfirmMessageReadResponse {

  @SerializedName("senderId")
  private String mSenderId;
  @SerializedName("messageId")
  private String mMessageId;

  public ConfirmMessageReadResponse(String senderId, String messageId) {
    mSenderId = senderId;
    mMessageId = messageId;
  }

  public ConfirmMessageReadResponse() {
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
