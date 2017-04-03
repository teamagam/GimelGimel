package com.teamagam.gimelgimel.data.alerts.entity;

import com.google.gson.annotations.SerializedName;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;

public class AlertData {

    @SerializedName("source")
    public String source;
    @SerializedName("time")
    public long time;
    @SerializedName("text")
    public String text;
    @SerializedName("severity")
    public int severity;
    @SerializedName("messageId")
    public String messageId;
    @SerializedName("geometry")
    public Point location;

    public AlertData(String source, long time, String text, int severity, String messageId) {
        this.source = source;
        this.time = time;
        this.text = text;
        this.severity = severity;
        this.messageId = messageId;
    }
}
