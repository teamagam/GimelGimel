package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class TextMessageResponse extends ServerResponse<String> {

  public TextMessageResponse(String text) {
    super(ServerResponse.TEXT);
    mContent = text;
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}
