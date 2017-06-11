package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatMessage {

  private String mMessageId;
  private String mSenderId;
  private Date mCreatedAt;
  private List<IMessageFeatureVisitable> mFeatures;

  public ChatMessage(String messageId,
      String senderId,
      Date createdAt,
      IMessageFeatureVisitable... features) {
    mFeatures = new ArrayList<>();
    mMessageId = messageId;
    mSenderId = senderId;
    mCreatedAt = createdAt;

    Collections.addAll(mFeatures, features);
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

  public List<IMessageFeatureVisitable> getFeatures() {
    return mFeatures;
  }

  public void addFeatures(IMessageFeatureVisitable... features) {
    Collections.addAll(mFeatures, features);
  }

  public void accept(IMessageFeatureVisitor visitor) {
    for (IMessageFeatureVisitable feature : mFeatures) {
      feature.accept(visitor);
    }
  }

  public <T extends IMessageFeatureVisitable> T getFeatureByType(Class<T> clazz) {
    int index = getFirstIndexOfFeature(clazz);

    if (index > -1) {
      return clazz.cast(mFeatures.get(index));
    }

    return null;
  }

  public boolean contains(Class<? extends IMessageFeatureVisitable> clazz) {
    return getFirstIndexOfFeature(clazz) > -1;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ChatMessage) {
      return mMessageId.equals(((ChatMessage) o).getMessageId());
    } else {
      return super.equals(o);
    }
  }

  private int getFirstIndexOfFeature(Class<? extends IMessageFeatureVisitable> clazz) {
    for (int i = 0; i < mFeatures.size(); i++) {
      IMessageFeatureVisitable feature = mFeatures.get(i);
      if (feature.getClass().equals(clazz)) {
        return i;
      }
    }

    return -1;
  }
}
