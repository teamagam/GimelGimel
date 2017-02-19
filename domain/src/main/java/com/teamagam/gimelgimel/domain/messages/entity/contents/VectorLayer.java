package com.teamagam.gimelgimel.domain.messages.entity.contents;

public class VectorLayer {

    private final String mId;
    private String mName;
    private int mVersion;
    private Severity mSeverity;

    public VectorLayer(String id, String name, int version) {
        this(id, name, version, Severity.REGULAR);
    }

    public VectorLayer(String id, String name, int version, Severity severity) {
        mId = id;
        mName = name;
        mVersion = version;
        mSeverity = severity;
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

    public boolean isImportant() {
        return mSeverity == Severity.IMPORTANT;
    }

    @Override
    public String toString() {
        return String.format("ID=%s \r\n NAME=%s \r\n VERSION=%d \r\n", mId, mName, mVersion);
    }

    public enum Severity {
        REGULAR, IMPORTANT
    }
}
