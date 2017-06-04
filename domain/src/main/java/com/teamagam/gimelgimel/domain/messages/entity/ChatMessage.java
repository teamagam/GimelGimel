package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.features.MessageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatMessage implements IMessageVisitable {

  private String mMessageId;
  private String mSenderId;
  private Date mCreatedAt;
  private List<MessageFeature> mFeatures;

  public ChatMessage(String messageId, String senderId, Date createdAt,
      MessageFeature... features) {
    mFeatures = new ArrayList<>();

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

  public List<MessageFeature> getFeatures() {
    return mFeatures;
  }

  public void addFeatures(MessageFeature... features) {
    Collections.addAll(mFeatures, features);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Message) {
      return mMessageId.equals(((Message) o).getMessageId());
    } else {
      return super.equals(o);
    }
  }

  @Override
  public void accept(IMessageVisitor visitor) {

  }
}
