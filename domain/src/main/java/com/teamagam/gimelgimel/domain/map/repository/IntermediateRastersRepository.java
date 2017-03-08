package com.teamagam.gimelgimel.domain.map.repository;


import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;

public interface IntermediateRastersRepository {

    void add(IntermediateRaster raster);

    IntermediateRaster get(String name);

    Iterable<IntermediateRaster> getAll();
}
