package com.teamagam.gimelgimel.domain.map.entities.symbols;

public class PointSymbol implements Symbol {

    private String mType;

    public PointSymbol(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

}
