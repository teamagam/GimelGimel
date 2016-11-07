package com.teamagam.gimelgimel.app.message.model.contents;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentApp {

    private PointGeometryApp point;

    private String text;

    private String geoContentText;

    private String type;

    public GeoContentApp(PointGeometryApp point){
        this.point = point;
    }

    public GeoContentApp(PointGeometryApp point, String text, String type, String geoContentText) {
        this.point = point;
        this.text = text;
        this.type = type;
        this.geoContentText = geoContentText;
    }

    public void setPoint(PointGeometryApp point) {
        this.point = point;
    }

    public PointGeometryApp getPointGeometry() {
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

    public String getGeoContentText() {
        return geoContentText;
    }

    public void setGeoContentText(String geoContentText) {
        this.geoContentText = geoContentText;
    }
}
