package com.teamagam.gimelgimel.domain.map.entities.interfaces;

public interface IGeoEntityVisitable {
  void accept(IGeoEntityVisitor visitor);
}
