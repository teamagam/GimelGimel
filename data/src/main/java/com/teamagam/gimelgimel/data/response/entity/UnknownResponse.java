package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;
import java.util.Date;

public class UnknownResponse extends ServerResponse {
  public UnknownResponse(Date date) {
    super(DUMMY);
    setCreatedAt(date);
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}
