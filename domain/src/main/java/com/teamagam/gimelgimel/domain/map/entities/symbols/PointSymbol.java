package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PointSymbol implements Symbol {

    private String mType;

    public PointSymbol(String type) {
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
