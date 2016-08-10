package com.teamagam.gimelgimel.domain.geometries.entities.interfaces;

import com.teamagam.gimelgimel.domain.geometries.entities.MultiPointGeometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

public interface IGeometryVisitor {
    void visit(PointGeometry geometry);
    void visit(MultiPointGeometry geometry);
}
