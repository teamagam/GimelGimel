package com.teamagam.gimelgimel.domain.messages.entity.visitor;

public interface IMessageFeatureVisitable {
  void accept(IMessageFeatureVisitor visitor);
}
