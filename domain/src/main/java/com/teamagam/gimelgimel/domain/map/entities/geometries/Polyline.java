package com.teamagam.gimelgimel.domain.map.entities.geometries;


import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.List;

public class Polyline extends AbsPointsGeometry {

    public Polyline(List<PointGeometry> points) {
        super(points);
    }

    @Override
    public void accept(IGeometryVisitor visitor) {
        visitor.visit(this);
    }
}
