package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Data-Type class for {@link Message}'s inner content
 */
public class MessageContent {

    @SerializedName("text")
    private String mText = null;

    @SerializedName("point")
    private PointGeometry mPointGeometry;

    public MessageContent(String text) {
        mText = text;
    }

    public MessageContent(PointGeometry point) {
        mPointGeometry = point;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
