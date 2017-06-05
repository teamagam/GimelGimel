package com.teamagam.gimelgimel.domain.messages.entity.visitor;

public interface IFeatureMessageVisitable {
  void accept(IMessageFeatureVisitor visitor);
}
