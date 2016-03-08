package com.teamagam.gimelgimel.app.view.viewer.data.symbols;

/**
 * Created by Bar on 03-Mar-16.
 */
public abstract class ColoredSymbol implements Symbol {

    protected String mCssColor;

    public ColoredSymbol(String cssColor) {
        mCssColor = cssColor;
    }

    public String getCssColor() {
        return mCssColor;
    }

    public void setCssColor(String cssColor) {
        mCssColor = cssColor;
    }
}
