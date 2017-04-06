package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.geogson.core.model.Geometry;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentData {

    @SerializedName("geometry")
    private Geometry mGeometry;

    @SerializedName("text")
    private String mText;

    @SerializedName("locationType")
    private String mLocationType;

    public GeoContentData(Geometry geometry, String text, String locationType) {
        mGeometry = geometry;
        mText = text;
        mLocationType = locationType;
    }

    public GeoContentData(Geometry geometry, String text) {
        mGeometry = geometry;
        mText = text;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public String getText() {
        return mText;
    }

    public String getLocationType() {
        return mLocationType;
    }
}
