package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;
import java.util.Date;

public class UnknownResponse extends GGResponse {
  public UnknownResponse(Date date) {
    super(DUMMY);
    setCreatedAt(date);
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}
