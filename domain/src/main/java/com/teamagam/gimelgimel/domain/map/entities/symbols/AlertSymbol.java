package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class AlertSymbol extends BaseSymbol {

    private final int mSeverity;

    public AlertSymbol(boolean isSelected, int severity) {
        super(isSelected);
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