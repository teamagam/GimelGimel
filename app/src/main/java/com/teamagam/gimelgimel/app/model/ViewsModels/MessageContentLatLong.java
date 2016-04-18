package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created by Yoni on 4/18/2016.
 * LatLong-Type class for {@link Message}'s inner content
 */
public class MessageContentLatLong implements MessageContentInterface{

    @SerializedName("point")
    private PointGeometry mPointGeometry;

    public MessageContentLatLong(PointGeometry point) {
        mPointGeometry = point;
    }

    public PointGeometry getPoint() {
        return mPointGeometry;
    }

    public void setPoint(PointGeometry point) {
        mPointGeometry = point;
    }
}
