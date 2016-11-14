package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitable;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

/**
 * Immutable geoEntity
 */
public interface GeoEntity extends IGeoEntityVisitable {

    String getId();

    Geometry getGeometry();

    String getText();

    Symbol getSymbol();

    String getLayerTag();

    void setLayerTag(String layerId);
}
