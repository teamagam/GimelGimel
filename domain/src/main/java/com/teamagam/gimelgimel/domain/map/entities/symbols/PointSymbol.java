package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PointSymbol extends BaseSymbol {

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