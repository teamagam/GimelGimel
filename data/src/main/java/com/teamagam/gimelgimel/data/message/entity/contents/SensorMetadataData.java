package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;

public class SensorMetadataData {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("location")
    private PointGeometryData mPointGeometryData;

    public SensorMetadataData(String id,
                              String name,
                              PointGeometryData pointGeometryData) {
        mId = id;
        mName = name;
        mPointGeometryData = pointGeometryData;
    }


    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public PointGeometryData getPointGeometryData() {
        return mPointGeometryData;
    }

    public void setPointGeometryData(
            PointGeometryData mPointGeometryData) {
        this.mPointGeometryData = mPointGeometryData;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
