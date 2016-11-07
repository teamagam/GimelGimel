package com.teamagam.gimelgimel.domain.map.entities.symbols;

public class PointSymbol implements Symbol {

    private String mType;
    private String mText;

    public PointSymbol(String type, String text) {
        mType = type;
        mText = text;
    }

    public String getType() {
        return mType;
    }

    public String getText() {
        return mText;
    }

}
