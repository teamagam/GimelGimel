package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class AlertSymbol implements Symbol {

    private final int mSeverity;

    public AlertSymbol(int severity) {
        mSeverity = severity;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }

    public int getSeverity() {
        return mSeverity;
    }
}
