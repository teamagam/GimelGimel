package com.teamagam.gimelgimel.data.response.entity;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public abstract class GGResponse<T> implements IResponseVisitable {

  public static final String DUMMY = "Dummy";
  public static final String TEXT = "Text";
  public static final String GEO = "Geo";
  public static final String USER_LOCATION = "UserLocation";
  public static final String IMAGE = "Image";
  public static final String SENSOR = "Sensor";
  public static final String ALERT = "Alert";
  public static final String VECTOR_LAYER = "VectorLayer";
  @SerializedName("content")
  protected T mContent;
  @SerializedName("_id")
  private String mMessageId;
  @SerializedName("senderId")
  private String mSenderId;
  @SerializedName("createdAt")
  private Date mCreatedAt;
  @SerializedName("type")
  private
  @MessageType
  String mType;

  public GGResponse(@MessageType String type) {
    this.mType = type;
  }

  public T getContent() {
    return mContent;
  }

  public void setContent(T content) {
    mContent = content;
  }

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
    this.mSenderId = senderId;
  }

  public Date getCreatedAt() {
    return mCreatedAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.mCreatedAt = createdAt;
  }

  public
  @MessageType
  String getType() {
    return mType;
  }

  @Retention(RetentionPolicy.SOURCE)
  public @interface MessageType {
  }
}
