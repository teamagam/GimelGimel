package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.MultiPointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.PointGeometry;

public interface IGeometryVisitor {
    void visit(PointGeometry geometry);
    void visit(MultiPointGeometry geometry);
}
