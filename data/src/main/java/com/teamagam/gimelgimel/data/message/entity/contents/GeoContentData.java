package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.map.entity.GeometryData;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentData {

    @SerializedName("location")
    private GeometryData mGeometry;

    @SerializedName("text")
    private String mText;

    @SerializedName("locationType")
    private String mType;


    public GeoContentData(GeometryData geometry, String text, String type) {
        this.mGeometry = geometry;
        this.mText = text;
        this.mType = type;
    }

    public GeometryData getGeometry() {
        return mGeometry;
    }

    public String getText() {
        return mText;
    }

    public String getType() {
        return mType;
    }
}
