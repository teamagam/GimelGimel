package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitable;

/**
 * Immutable geoEntity
 */
public interface GeoEntity extends IGeoEntityVisitable {

    String getId();

    Geometry getGeometry();

    String getLayerTag();

    void setLayerTag(String layerId);
}
