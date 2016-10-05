package com.teamagam.gimelgimel.domain.map.entities;

/**
 * Immutable geoEntity
 */
public interface GeoEntity {

    String getId();

    Geometry getGeometry();

    Symbol getSymbol();

    String getLayerTag();

}
