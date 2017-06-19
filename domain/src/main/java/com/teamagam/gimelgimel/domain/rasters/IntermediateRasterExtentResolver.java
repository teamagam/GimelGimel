package com.teamagam.gimelgimel.domain.rasters;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;

public interface IntermediateRasterExtentResolver {

  Geometry getExtent(IntermediateRasterPresentation irp);
}