package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class TextFeature implements IMessageFeatureVisitable {

  private String mTitle;
  private String mText;

  public TextFeature(String text) {
    mText = text;
  }

  public TextFeature(String title, String text) {
    this(text);
    mTitle = title;
  }

  public String getText() {
    return mText;
  }

  public String getTitle() {
    return mTitle;
  }

  @Override
  public void accept(IMessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
