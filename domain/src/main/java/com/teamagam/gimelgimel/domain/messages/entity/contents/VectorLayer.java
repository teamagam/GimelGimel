package com.teamagam.gimelgimel.domain.messages.entity.contents;

public class VectorLayer {

    private final String mId;
    private String mName;
    private int mVersion;

    public VectorLayer(String id, String name, int version) {
        mId = id;
        mName = name;
        mVersion = version;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getVersion() {
        return mVersion;
    }

    @Override
    public String toString() {
        return String.format("ID=%s \r\n NAME=%s \r\n VERSION=%d \r\n", mId, mName, mVersion);
    }
}
