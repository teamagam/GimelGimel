package com.teamagam.gimelgimel.domain.geometries.entities.interfaces;

public interface IGeometryVisitable {
    void accept(IGeometryVisitor visitor);
}
