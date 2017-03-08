package com.teamagam.gimelgimel.domain.layers;


import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;

public interface IntermediateRasterLocalStorage {
    Iterable<IntermediateRaster> getExistingLayers();
}
