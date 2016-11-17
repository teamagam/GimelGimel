package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

/**
 * Created on 11/14/2016.
 * for future use.
 */

public class ImageSymbol implements Symbol {

    @Override
    public void accept(ISymbolVisitor visitor) {
        visitor.visit(this);
    }
}
