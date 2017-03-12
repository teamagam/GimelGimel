package com.teamagam.gimelgimel.domain.rasters.repository;


import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;

public interface IntermediateRastersRepository {

    void add(IntermediateRaster raster);

    IntermediateRaster get(String name);

    Iterable<IntermediateRaster> getAll();
}
