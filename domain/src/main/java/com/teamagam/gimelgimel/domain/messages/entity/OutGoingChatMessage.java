package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OutGoingChatMessage {
  private String mSenderId;
  private List<MessageFeatureVisitable> mFeatures;

  public OutGoingChatMessage(String senderId, MessageFeatureVisitable... features) {
    mFeatures = new ArrayList<>();
    mSenderId = senderId;
    addFeatures(features);
  }

  public String getSenderId() {
    return mSenderId;
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
