package com.teamagam.gimelgimel.domain.map.entities.interfaces;

public interface GeoEntityVisitable {
  void accept(GeoEntityVisitor visitor);
}
