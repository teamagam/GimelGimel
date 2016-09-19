package com.teamagam.gimelgimel.domain.geometries.entities;

public interface GeoEntity {

    String getId();

    Geometry getGeometry();

    Symbol getSymbol();

    void updateGeometry(Geometry geo);

    void updateSymbol(Symbol symbol);
}
