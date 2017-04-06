package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PointSymbol extends BaseSymbol {

    public static String POINT_TYPE_BUILDING = "building";
    public static String POINT_TYPE_ENEMY = "enemy";
    public static String POINT_TYPE_GENERAL = "general";
    public static String POINT_TYPE_CIRCLE = "circle";

    private String mType;

    public PointSymbol(boolean isSelected, String type) {
        super(isSelected);
        mType = type;
    }

    public String getType() {
        return mType;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }
}