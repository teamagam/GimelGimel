package com.teamagam.gimelgimel.domain.layers.entitiy;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.net.URI;

public class VectorLayerPresentation extends VectorLayer {

    public static VectorLayerPresentation createShown(VectorLayer vectorLayer, URI localURI){
        return create(vectorLayer, localURI, true);
    }

    public static VectorLayerPresentation createHidden(VectorLayer vectorLayer, URI localURI){
        return create(vectorLayer, localURI, false);
    }

    private static VectorLayerPresentation create(VectorLayer vectorLayer, URI localURI,
                                                 boolean isShown) {
        return new VectorLayerPresentation(vectorLayer.getId(),
                vectorLayer.getName(),
                vectorLayer.getVersion(),
                localURI, isShown);
    }

    private URI mLocalURI;
    private boolean mIsShown;

    private VectorLayerPresentation(String id, String name, int version, URI
            localURI, boolean isShown) {
        super(id, name, version);
        mLocalURI = localURI;
        mIsShown = isShown;
    }

    public URI getLocalURI() {
        return mLocalURI;
    }

    public boolean isShown() {
        return mIsShown;
    }
}
