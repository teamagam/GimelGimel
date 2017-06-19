package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;

@Entity
public class ChatMessageEntity {
  @ColumnInfo(name = "id")
  @PrimaryKey
  private String mMessageId;

  @ColumnInfo(name = "sender_id")
  private String mSenderId;

  @ColumnInfo(name = "creation_date")
  private Date mCreationDate;

  public String getMessageId() {
    return mMessageId;
  }

  public void setMessageId(String messageId) {
    mMessageId = messageId;
  }

  public String getSenderId() {
    return mSenderId;
  }

  public void setSenderId(String senderId) {
    mSenderId = senderId;
  }

  public Date getCreationDate() {
    return mCreationDate;
  }

  public void setCreationDate(Date creationDate) {
    mCreationDate = creationDate;
  }
}
