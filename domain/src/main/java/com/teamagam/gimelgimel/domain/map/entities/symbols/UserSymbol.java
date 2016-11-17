package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

/**
 * Created on 11/17/2016.
 * TODO: complete text
 */

public class UserSymbol implements Symbol {

    private final boolean mIsActive;

    private String mUserName;

    public UserSymbol(String userName, boolean isActive) {
        mIsActive = isActive;
        mUserName = userName;
    }

    public String getUserName() {
        return mUserName;
    }

    public boolean isActive() {
        return mIsActive;
    }


    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }
}
