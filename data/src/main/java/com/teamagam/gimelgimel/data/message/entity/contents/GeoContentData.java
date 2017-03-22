package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.map.entity.GeometryData;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.data.map.entity.PolygonData;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentData {

    @SerializedName("location")
    private PointGeometryData mPointGeometry;

    @SerializedName("geometry")
    private PolygonData mPolygon;

    @SerializedName("text")
    private String mText;

    @SerializedName("locationType")
    private String mLocationType;

    public GeoContentData(PointGeometryData geometry, String text, String locationType) {
        mPointGeometry = geometry;
        mText = text;
        mLocationType = locationType;
    }

    public GeoContentData(PolygonData polygonData, String text) {
        mPolygon = polygonData;
        mText = text;
    }

    public GeometryData getGeometry() {
        return mPointGeometry != null ? mPointGeometry : mPolygon;
    }

    public String getText() {
        return mText;
    }

    public String getLocationType() {
        return mLocationType;
    }
}
