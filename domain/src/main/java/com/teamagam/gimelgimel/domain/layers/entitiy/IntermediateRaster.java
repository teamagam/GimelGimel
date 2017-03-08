package com.teamagam.gimelgimel.domain.layers.entitiy;


import java.net.URI;

public class IntermediateRaster {
    private String mName;
    private URI mUri;

    public IntermediateRaster(String name, URI uri) {
        mName = name;
        mUri = uri;
    }

    public String getName() {
        return mName;
    }

    public URI getUri() {
        return mUri;
    }
}
