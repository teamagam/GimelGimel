package com.teamagam.gimelgimel.domain.map.entities.interfaces;

public interface IGeometryVisitable {
  void accept(IGeometryVisitor visitor);
}
