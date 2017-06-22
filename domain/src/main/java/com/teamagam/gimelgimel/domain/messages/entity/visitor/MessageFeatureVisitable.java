package com.teamagam.gimelgimel.domain.messages.entity.visitor;

public interface MessageFeatureVisitable {
  void accept(MessageFeatureVisitor visitor);
}
