package com.teamagam.gimelgimel.domain.geo.entity;

import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;

public interface GeoEntity {

    String getId();

    Geometry getGeometry();

    Symbol getSymbol();

    void updateGeometry(Geometry geo);

    void updateSymbol(Symbol symbol);
}
