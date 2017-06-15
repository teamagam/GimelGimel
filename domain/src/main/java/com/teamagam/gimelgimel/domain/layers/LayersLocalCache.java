package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.net.URI;
import java.net.URL;
import io.reactivex.Observable;

public interface LayersLocalCache {

  Observable<URI> cache(VectorLayer vectorLayer);

  boolean isCached(VectorLayer vectorLayer);

  URI getCachedURI(VectorLayer vectorLayer);

  Iterable<VectorLayer> getAllCachedLayers();
}
