package com.teamagam.gimelgimel.domain.map.entities.interfaces;

public interface GeometryVisitable {
  void accept(GeometryVisitor visitor);
}
