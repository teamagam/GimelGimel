package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeoEntityVisitable;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;

public interface GeoEntity extends IGeoEntityVisitable {

  String getId();

  String getText();

  Geometry getGeometry();

  Symbol getSymbol();
}
