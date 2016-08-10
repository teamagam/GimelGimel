package com.teamagam.gimelgimel.domain.messages.entities.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

/**
 * A class represents a location pinned by the user
 */
public class GeoContent {

    @SerializedName("location")
    private PointGeometry mPoint;

    @SerializedName("text")
    private String mText;

    @SerializedName("locationType")
    private String mType;


    public GeoContent(PointGeometry point, String text, String type) {
        this.mPoint = point;
        this.mText = text;
        this.mType = type;
    }

    public PointGeometry getPointGeometry() {
        return mPoint;
    }

    public String getText() {
        return mText;
    }

    public String getType() {
        return mType;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        s.append("type=" + mType);
        s.append("point=" + mPoint);
        if (!mText.isEmpty()) {
            s.append("text=" + mText);
        } else {
            s.append("text=?");
        }
        s.append(']');
        return s.toString();
    }
}
