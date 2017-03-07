package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;

public class VectorLayerData {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("version")
    private int mVersion;

    @SerializedName("url")
    private String mRemoteUrl;

    @SerializedName("severity")
    private String mSeverity;

    @SerializedName("category")
    private String mCategory;


    public VectorLayerData(String id, String name, int version, String remoteUrl, String severity,
                           String category) {
        mId = id;
        mName = name;
        mVersion = version;
        mRemoteUrl = remoteUrl;
        mSeverity = severity;
        mCategory = category;
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

    public String getRemoteUrl() {
        return mRemoteUrl;
    }

    public String getSeverity() {
        return mSeverity;
    }

    public String getCategory() {
        return mCategory;
    }
}
