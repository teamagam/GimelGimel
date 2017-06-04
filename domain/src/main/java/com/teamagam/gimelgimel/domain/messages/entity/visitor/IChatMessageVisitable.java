package com.teamagam.gimelgimel.domain.messages.entity.visitor;

public interface IChatMessageVisitable {
  void accept(IMessageFeatureVisitor visitor);
}
