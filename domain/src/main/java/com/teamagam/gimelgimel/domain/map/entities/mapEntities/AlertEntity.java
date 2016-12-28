package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertSymbol;

public class AlertEntity extends AbsGeoEntity {

    private final PointGeometry mPoint;
    private final AlertSymbol mSymbol;

    public AlertEntity(String id, String text, PointGeometry point, int severity) {
        super(id, text);
        mPoint = point;
        mSymbol = new AlertSymbol(severity);
    }

    @Override
    public PointGeometry getGeometry() {
        return mPoint;
    }

    @Override
    public AlertSymbol getSymbol() {
        return mSymbol;
    }

    @Override
    public void accept(IGeoEntityVisitor visitor) {
        visitor.visit(this);
    }

}