package com.teamagam.gimelgimel.domain.map.entities.symbols;


import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PolylineSymbol extends BaseSymbol {

    private final String mText;

    public PolylineSymbol(boolean isSelected) {
        this(isSelected, null);
    }

    public PolylineSymbol(boolean isSelected, String text) {
        super(isSelected);
        mText = text;
    }

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return mText;
    }

    public boolean hasText() {
        return mText != null;
    }
}
