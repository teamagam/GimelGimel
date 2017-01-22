package com.teamagam.gimelgimel.domain.messages.entity.contents;

import java.net.URI;
import java.net.URL;

public class VectorLayer {

    private final String mId;
    private String mName;
    private URI mLocalUri;
    private URL mRemoteUrl;

    public VectorLayer(String id, String name, URL remoteUrl) {
        mId = id;
        mName = name;
        mRemoteUrl = remoteUrl;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public URI getLocalUri() {
        return mLocalUri;
    }

    public URL getRemoteUrl() {
        return mRemoteUrl;
    }

    public void setLocalUri(URI localUri) {
        mLocalUri = localUri;
    }

    @Override
    public String toString() {
        return String.format("ID=%s \r\n NAME=%s \r\n URL=%s \r\n URI=%s", mId, mName,
                mRemoteUrl, mLocalUri);
    }
}
