package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.net.URI;
import rx.Observable;

public interface LayersLocalCache {

  Observable<URI> cache(VectorLayer vectorLayerContent);

  boolean isCached(VectorLayer vectorLayerContent);

  URI getCachedURI(VectorLayer vectorLayerContent);

  Iterable<VectorLayer> getAllCachedLayers();
}
