package com.teamagam.gimelgimel.data.response.entity.visitor;

public interface ResponseVisitable {
  void accept(ResponseVisitor visitor);
}
