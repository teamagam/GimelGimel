package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OutGoingChatMessage {
  private String mSenderId;
  private Date mCreatedAt;
  private List<MessageFeatureVisitable> mFeatures;

  public OutGoingChatMessage(String senderId, Date createdAt, MessageFeatureVisitable... features) {
    mFeatures = new ArrayList<>();
    mSenderId = senderId;
    mCreatedAt = createdAt;
    addFeatures(features);
  }

  public String getSenderId() {
    return mSenderId;
  }

  public Date getCreatedAt() {
    return mCreatedAt;
  }

  public List<MessageFeatureVisitable> getFeatures() {
    return mFeatures;
  }

  public void addFeatures(MessageFeatureVisitable... features) {
    Collections.addAll(mFeatures, features);
  }

  public void accept(MessageFeatureVisitor visitor) {
    for (MessageFeatureVisitable feature : mFeatures) {
      feature.accept(visitor);
    }
  }

  public <T extends MessageFeatureVisitable> T getFeatureByType(Class<T> clazz) {
    int index = getFirstIndexOfFeature(clazz);

    if (index > -1) {
      return clazz.cast(mFeatures.get(index));
    }

    return null;
  }

  public boolean contains(Class<? extends MessageFeatureVisitable> clazz) {
    return getFirstIndexOfFeature(clazz) > -1;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ChatMessage) {
      return mCreatedAt.equals(((ChatMessage) o).getMessageId());
    } else {
      return super.equals(o);
    }
  }

  private int getFirstIndexOfFeature(Class<? extends MessageFeatureVisitable> clazz) {
    for (int i = 0; i < mFeatures.size(); i++) {
      MessageFeatureVisitable feature = mFeatures.get(i);
      if (feature.getClass().equals(clazz)) {
        return i;
      }
    }

    return -1;
  }
}
