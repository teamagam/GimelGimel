package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeoEntityVisitable;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public interface GeoEntity extends GeoEntityVisitable {

  String getId();

  Geometry getGeometry();

  Symbol getSymbol();
}
