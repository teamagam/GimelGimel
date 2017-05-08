package com.teamagam.gimelgimel.domain.map.entities.symbols;


import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class AlertPolygonSymbol extends BaseSymbol {

    public AlertPolygonSymbol(boolean isSelected) {
        super(isSelected);
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }
}
