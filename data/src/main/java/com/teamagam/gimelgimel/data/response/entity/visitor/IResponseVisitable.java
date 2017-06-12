package com.teamagam.gimelgimel.data.response.entity.visitor;

public interface IResponseVisitable {
  void accept(IResponseVisitor visitor);
}
