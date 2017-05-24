package com.teamagam.gimelgimel.data.rasters.repository;

import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRastersRepository;
import java.util.HashMap;
import java.util.Map;

public class IntermediateRastersRepositoryData implements IntermediateRastersRepository {

  private Map<String, IntermediateRaster> mRasters;

  public IntermediateRastersRepositoryData() {
    mRasters = new HashMap<>();
  }

  @Override
  public void add(IntermediateRaster raster) {
    mRasters.put(raster.getName(), raster);
  }

  @Override
  public IntermediateRaster get(String name) {
    return mRasters.get(name);
  }

  @Override
  public Iterable<IntermediateRaster> getAll() {
    return mRasters.values();
  }
}
