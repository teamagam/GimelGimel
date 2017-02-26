package com.teamagam.gimelgimel.domain.map.entities.symbols;


public abstract class BaseSymbol implements Symbol {

    private boolean mIsSelected;

    public BaseSymbol(boolean isSelected) {
        mIsSelected = isSelected;
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }
}