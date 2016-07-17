package com.teamagam.gimelgimel.app.model.entities;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Gil.Raytan on 10-Jul-16.
 * A class represents a location pined by the user
 */
public class GeoTextSample {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({REGULAR, ENEMY, OTHER})
    public @interface Type {}

    /**
     * type of the location that was pined by the user
     * (up to his decision)
     */
    public static final String REGULAR = "Regular";
    public static final String ENEMY = "Enemy";
    public static final String OTHER = "Other";

    @SerializedName("location")
    private PointGeometry mPoint;

    @SerializedName("text")
    private String mText;

    @Type
    @SerializedName("locationType")
    private String mType;


    public GeoTextSample(PointGeometry point, String text, @Type String type)
    {
        this.mPoint = point;
        this.mText = text;
        this.mType = type;
    }

    public PointGeometry getPointGeometry() {return mPoint;}
    public String getText() {return mText;}

    /** implementing toString function for the use of Gson q Json
     * while sending and recieving messages from the server
     **/
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        s.append("type="+ mType);
        s.append("point="+mPoint);
        if (!mText.isEmpty()) {
            s.append("text="+mText);
        } else {
            s.append("text=?");
        }
        s.append(']');
        return s.toString();
    }
}
