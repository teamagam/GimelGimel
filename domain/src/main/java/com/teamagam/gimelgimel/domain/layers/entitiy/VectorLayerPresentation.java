package com.teamagam.gimelgimel.domain.layers.entitiy;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.net.URI;

public class VectorLayerPresentation extends VectorLayer {

    public static VectorLayerPresentation create(VectorLayer vectorLayer, URI localURI) {
        return new VectorLayerPresentation(vectorLayer.getId(),
                vectorLayer.getName(),
                vectorLayer.getVersion(),
                localURI);
    }

    private URI mLocalURI;

    private VectorLayerPresentation(String id, String name, int version, URI
            localURI) {
        super(id, name, version);
        mLocalURI = localURI;
    }

    public URI getLocalURI() {
        return mLocalURI;
    }
}
