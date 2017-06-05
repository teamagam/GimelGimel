package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IFeatureMessageVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatMessage {

  private String mMessageId;
  private String mSenderId;
  private Date mCreatedAt;
  private List<IFeatureMessageVisitable> mFeatures;

  public ChatMessage(String messageId,
      String senderId,
      Date createdAt,
      IFeatureMessageVisitable... features) {
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

  public List<IFeatureMessageVisitable> getFeatures() {
    return mFeatures;
  }

  public void addFeatures(IFeatureMessageVisitable... features) {
    Collections.addAll(mFeatures, features);
  }

  public void accept(IMessageFeatureVisitor visitor) {
    for (IFeatureMessageVisitable feature : mFeatures) {
      feature.accept(visitor);
    }
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
