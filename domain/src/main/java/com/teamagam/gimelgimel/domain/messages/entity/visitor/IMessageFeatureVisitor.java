package com.teamagam.gimelgimel.domain.messages.entity.visitor;

import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;

public interface IMessageFeatureVisitor {
  void visit(TextFeature feature);
}
