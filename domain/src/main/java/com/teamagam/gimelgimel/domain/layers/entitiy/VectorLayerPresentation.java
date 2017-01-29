package com.teamagam.gimelgimel.domain.layers.entitiy;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.net.URI;
import java.net.URL;

public class VectorLayerPresentation extends VectorLayer {

    public static VectorLayerPresentation create(VectorLayer vectorLayer, URI localURI) {
        return new VectorLayerPresentation(vectorLayer.getId(),
                vectorLayer.getName(),
                vectorLayer.getRemoteUrl(),
                localURI);
    }

    private URI mLocalURI;

    private VectorLayerPresentation(String id, String name, URL remoteUrl, URI localURI) {
        super(id, name, remoteUrl);
        mLocalURI = localURI;
    }

    public URI getLocalURI() {
        return mLocalURI;
    }
}
