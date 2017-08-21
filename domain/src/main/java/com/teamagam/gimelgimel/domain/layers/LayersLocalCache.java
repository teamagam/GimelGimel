package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.net.URI;

public interface LayersLocalCache {

  URI cache(VectorLayer vectorLayer);

  boolean isCached(VectorLayer vectorLayer);

  URI getCachedURI(VectorLayer vectorLayer);
}
