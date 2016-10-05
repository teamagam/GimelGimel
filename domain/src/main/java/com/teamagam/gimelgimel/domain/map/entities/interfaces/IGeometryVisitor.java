package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public interface IGeometryVisitor {
    void visit(PointGeometry geometry);
    void visit(MultiPointGeometry geometry);
}
