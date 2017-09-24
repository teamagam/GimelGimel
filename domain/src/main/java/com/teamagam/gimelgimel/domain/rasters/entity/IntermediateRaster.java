package com.teamagam.gimelgimel.domain.rasters.entity;

import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import java.net.URI;

public class IntermediateRaster implements IdentifiedData {
  private String mName;
  private URI mUri;

  public IntermediateRaster(String name, URI uri) {
    mName = name;
    mUri = uri;
  }

  public String getName() {
    return mName;
  }

  public URI getLocalUri() {
    return mUri;
  }

  @Override
  public String getId() {
    return mName;
  }
}
