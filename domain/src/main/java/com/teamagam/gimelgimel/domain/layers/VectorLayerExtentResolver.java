package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;

public interface VectorLayerExtentResolver {
  Geometry getExtent(VectorLayerPresentation vectorLayer);
}