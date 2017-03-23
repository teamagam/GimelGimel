package com.teamagam.gimelgimel.domain.map.entities.geometries;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

import java.util.List;

public class Polygon implements Geometry {

    private List<PointGeometry> mPoints;

    public Polygon(List<PointGeometry> points) {
        mPoints = points;
    }

    @Override
    public void accept(IGeometryVisitor visitor) {
        visitor.visit(this);
    }

    public List<PointGeometry> getPoints() {
        return mPoints;
    }
}