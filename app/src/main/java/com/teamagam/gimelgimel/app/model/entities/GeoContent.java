package com.teamagam.gimelgimel.app.model.entities;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * A class represents a location pinned by the user
 */
public class GeoContent {

    @SerializedName("location")
    private PointGeometry point;

    @SerializedName("text")
    private String text;

    @SerializedName("locationType")
    private String type;

    public GeoContent(PointGeometry point){
        this.point = point;
    }

    public GeoContent(PointGeometry point, String text, String type) {
        this.point = point;
        this.text = text;
        this.type = type;
    }

    public void setPoint(PointGeometry point) {
        this.point = point;
    }

    public PointGeometry getPointGeometry() {
        return point;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        s.append("type=" + type);
        s.append("point=" + point);
        if (!text.isEmpty()) {
            s.append("text=" + text);
        } else {
            s.append("text=?");
        }
        s.append(']');
        return s.toString();
    }
}
