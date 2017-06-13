package com.teamagam.gimelgimel.domain.messages.entity.visitor;

import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;

public interface IMessageFeatureVisitor {
  void visit(TextFeature feature);

  void visit(GeoFeature feature);

  void visit(ImageFeature feature);

  void visit(AlertFeature feature);
}
