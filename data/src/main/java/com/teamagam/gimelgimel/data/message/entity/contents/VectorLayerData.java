package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;

public class VectorLayerData {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("url")
    private String mRemoteUrl;

    public VectorLayerData(String id, String name, String remoteUrl) {
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

    public String getRemoteUrl() {
        return mRemoteUrl;
    }
}
