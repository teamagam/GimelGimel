package com.teamagam.gimelgimel.domain.map.entities.symbols;

/**
 * Created on 11/17/2016.
 * TODO: complete text
 */

public class UserSymbol implements Symbol {

    private final boolean mIsActive;

    public UserSymbol(boolean isActive) {
        mIsActive = isActive;
    }

    public boolean isActive() {
        return mIsActive;
    }


}
