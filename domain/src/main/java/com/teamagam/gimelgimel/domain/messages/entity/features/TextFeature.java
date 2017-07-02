package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;

public class TextFeature implements MessageFeatureVisitable {

  private String mText;

  public TextFeature(String text) {
    mText = text;
  }

  public String getText() {
    return mText;
  }

  @Override
  public void accept(MessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
