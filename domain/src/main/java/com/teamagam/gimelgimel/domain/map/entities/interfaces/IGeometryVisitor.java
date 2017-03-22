package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;

public interface IGeometryVisitor {
    void visit(PointGeometry geometry);

    void visit(Polygon polygon);
}
