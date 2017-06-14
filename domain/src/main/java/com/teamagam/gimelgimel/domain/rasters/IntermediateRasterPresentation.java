package com.teamagam.gimelgimel.domain.rasters;

import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import java.net.URI;

public class IntermediateRasterPresentation extends IntermediateRaster {

  private boolean mIsShown;

  public IntermediateRasterPresentation(String name, URI uri, boolean isShown) {
    super(name, uri);
    mIsShown = isShown;
  }

  public boolean isShown() {
    return mIsShown;
  }
}