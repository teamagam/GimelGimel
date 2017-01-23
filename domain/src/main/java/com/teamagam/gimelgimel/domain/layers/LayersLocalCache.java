package com.teamagam.gimelgimel.domain.layers;


import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.net.URI;

import rx.Observable;

public interface LayersLocalCache {

    Observable<URI> cache(VectorLayer vectorLayer);

    boolean isCached(VectorLayer vectorLayer);

    URI getCachedURI(VectorLayer vectorLayer);
}
