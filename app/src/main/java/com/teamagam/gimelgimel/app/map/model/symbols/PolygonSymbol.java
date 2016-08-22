package com.teamagam.gimelgimel.app.map.model.symbols;

/**
 * Created by Bar on 03-Mar-16.
 */
public class PolygonSymbol implements Symbol {

    public static PolygonSymbol DEFAULT = new PolygonSymbol("#FF0000", "#000000", 0.5);

    private String mInnerCssColor;
    private String mOutlineCssColor;
    private double mInnerColorAlpha;

    public PolygonSymbol(String innerCssColor, String outlineCssColor, double innerColorAlpha) {
        mInnerCssColor = innerCssColor;
        mOutlineCssColor = outlineCssColor;
        mInnerColorAlpha = innerColorAlpha;
    }

    public String getInnerCssColor() {
        return mInnerCssColor;
    }

    public String getOutlineCssColor() {
        return mOutlineCssColor;
    }

    public double getInnerColorAlpha() {
        return mInnerColorAlpha;
    }
}
