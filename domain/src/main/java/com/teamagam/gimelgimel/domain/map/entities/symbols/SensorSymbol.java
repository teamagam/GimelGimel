package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class SensorSymbol implements Symbol {


    @Override
    public void accept(ISymbolVisitor visitor) {
            visitor.visit(this);
    }
}
