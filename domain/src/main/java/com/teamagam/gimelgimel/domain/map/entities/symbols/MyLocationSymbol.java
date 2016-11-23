package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class MyLocationSymbol implements Symbol {

    private final boolean mIsActive;

    public MyLocationSymbol(boolean mIsActive) {
        this.mIsActive = mIsActive;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }

    public boolean isIsActive() {
        return mIsActive;
    }
}
