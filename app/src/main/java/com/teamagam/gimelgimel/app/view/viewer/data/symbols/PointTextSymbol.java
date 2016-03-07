package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

import android.graphics.Color;

import com.teamagam.gimelgimel.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bar on 03-Mar-16.
 */
public class PointTextSymbol extends ColoredSymbol implements PointSymbol {

    private String mText;
    private int mSize;

    public PointTextSymbol(String color, String text, int size) {
        super(color);
        this.mText = text;
        this.mSize = size;
    }


/*
    //Should not be here. Should be inside cesium handling class
    @Override
    public String toJson() {

        return String.format(
                "{image: pinBuilder.fromText('%s', Cesium.Color.fromCssColorString('%s'), %s).toDataURL(),\n" +
                "                verticalOrigin: Cesium.VerticalOrigin.BOTTOM}"
                , mText, mCssColor, mSize);
    }
*/

    public String getText() {
        return mText;
    }

    public int getSize() {
        return mSize;
    }
}
