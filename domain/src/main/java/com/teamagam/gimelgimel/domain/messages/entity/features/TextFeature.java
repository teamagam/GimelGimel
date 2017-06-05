package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IFeatureMessageVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class TextFeature implements IFeatureMessageVisitable {

  private String mText;

  public TextFeature(String text) {
    mText = text;
  }

  public String getText() {
    return mText;
  }

  @Override
  public void accept(IMessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
