package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Data-Type class for {@link Message}'s inner content
 */
public class MessageContent {

    @SerializedName("text")
    private String mText = null;

    /**
     * We want to avoid deserialize of long/lat fields in-case they're not initialized.
     * Objects are is initialized with null.
     */
    @SerializedName("point")
    private PointGeometry mPointGeometry;

    public MessageContent(String text) {
        mText = text;
    }

    //this is right way to do it. W/O float
    public MessageContent(PointGeometry point) {
        mPointGeometry = point;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public PointGeometry getPoint() {
        return mPointGeometry;
    }

    public void setPoint(PointGeometry point) {
        mPointGeometry = point;
    }
}
