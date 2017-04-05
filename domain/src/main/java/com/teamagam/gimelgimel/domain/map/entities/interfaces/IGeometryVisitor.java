package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;

public interface IGeometryVisitor {
    void visit(PointGeometry point);

    void visit(Polygon polygon);

    void visit(Polyline polyline);
}
