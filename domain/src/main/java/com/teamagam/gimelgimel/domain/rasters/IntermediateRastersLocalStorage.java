package com.teamagam.gimelgimel.domain.rasters;


import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;

public interface IntermediateRastersLocalStorage {
    Iterable<IntermediateRaster> getExistingRasters();
}
