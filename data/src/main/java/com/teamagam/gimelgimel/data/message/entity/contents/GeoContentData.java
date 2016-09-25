package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;

import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentData {

    @SerializedName("location")
    private PointGeometryData mPoint;

    @SerializedName("text")
    private String mText;

    @SerializedName("locationType")
    private String mType;


    public GeoContentData(PointGeometryData point, String text, String type) {
        this.mPoint = point;
        this.mText = text;
        this.mType = type;
    }

    public PointGeometryData getPointGeometry() {
        return mPoint;
    }

    public String getText() {
        return mText;
    }

    public String getType() {
        return mType;
    }
}
