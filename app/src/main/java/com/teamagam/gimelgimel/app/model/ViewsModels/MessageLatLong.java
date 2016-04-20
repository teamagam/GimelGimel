package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * LatLong-Type class for {@link Message}'s inner content
 */
public class MessageLatLong extends Message<PointGeometry>{

    public MessageLatLong(String senderId, PointGeometry point) {
        super(senderId, Message.LAT_LONG);
        mContent = point;
    }

    public PointGeometry getPoint() {
        return mContent;
    }

    public void setPoint(PointGeometry point) {
        mContent = point;
    }

}
