package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertSymbol;

public class AlertEntity extends AbsGeoEntity {

    private final Geometry mGeometry;
    private final AlertSymbol mSymbol;

    public AlertEntity(String id, String text, Geometry point, int severity,
                       boolean isSelected) {
        super(id, text);
        mGeometry = point;
        mSymbol = new AlertSymbol(isSelected, severity);
    }

    @Override
    public Geometry getGeometry() {
        return mGeometry;
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