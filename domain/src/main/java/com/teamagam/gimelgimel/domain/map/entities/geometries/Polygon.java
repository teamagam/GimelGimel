package com.teamagam.gimelgimel.domain.map.entities.geometries;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.List;

public class Polygon extends AbsPointsGeometry {

    public Polygon(List<PointGeometry> points) {
        super(points);
    }

    @Override
    public void accept(IGeometryVisitor visitor) {
        visitor.visit(this);
    }
}