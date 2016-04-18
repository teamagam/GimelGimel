package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yoni on 4/18/2016.
 * Text-Type class for {@link Message}'s inner content
 */
public class MessageContentText implements MessageContentInterface{

    @SerializedName("text")
    private String mText;

    public MessageContentText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
