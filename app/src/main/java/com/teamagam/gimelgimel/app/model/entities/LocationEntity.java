package com.teamagam.gimelgimel.app.model.entities;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Gil.Raytan on 10-Jul-16.
 */
public class LocationEntity {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({REGULAR, ENEMY, OTHER})
    public @interface Type {}
    public static final String REGULAR = "Regular";
    public static final String ENEMY = "Enemy";
    public static final String OTHER = "Other";

    @SerializedName("location")
    private PointGeometry mPoint;

    @SerializedName("text")
    private String mText;

    @Type
    @SerializedName("locationType")
    private String mTypoe;


    public LocationEntity(PointGeometry point, String text,@Type String type)
    {
        this.mPoint = point;
        this.mText = text;
        this.mTypoe = type;
    }

    public PointGeometry getGeometry() {return mPoint;}
    public String getText() {return mText;}

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        s.append("type="+mTypoe);
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
