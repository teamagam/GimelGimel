package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

/**
 * Created by Bar on 03-Mar-16.
 */
public class PolylineSymbol implements Symbol {

    public static PolylineSymbol DEFAULT = new PolylineSymbol(5, "#6666FF");

    private int mWidth;
    private String mCssColor;

    public PolylineSymbol(int width, String cssColor) {
        mWidth = width;
        mCssColor = cssColor;
    }

    public int getWidth() {
        return mWidth;
    }

    public String getCssColor() {
        return mCssColor;
    }
}
