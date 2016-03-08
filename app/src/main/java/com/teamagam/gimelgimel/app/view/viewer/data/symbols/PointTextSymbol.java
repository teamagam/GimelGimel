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

    public static PointTextSymbol DEFAULT = new PointTextSymbol("#6666FF", " ", 48);

    private String mText;
    private int mSize;

    public PointTextSymbol(String color, String text, int size) {
        super(color);
        this.mText = text;
        this.mSize = size;
    }

    public String getText() {
        return mText;
    }

    public int getSize() {
        return mSize;
    }
}
