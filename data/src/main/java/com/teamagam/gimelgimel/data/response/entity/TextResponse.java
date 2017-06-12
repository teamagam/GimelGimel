package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;

/**
 * Text-Type class for {@link GGResponse}'s inner content
 */
public class TextResponse extends GGResponse<String> {

  public TextResponse(String text) {
    super(GGResponse.TEXT);
    mContent = text;
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}
