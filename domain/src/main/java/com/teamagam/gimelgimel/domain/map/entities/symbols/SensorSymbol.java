package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class SensorSymbol implements Symbol {

    private final String mName;

    public SensorSymbol(String name) {
        mName = name;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return mName;
    }
}
