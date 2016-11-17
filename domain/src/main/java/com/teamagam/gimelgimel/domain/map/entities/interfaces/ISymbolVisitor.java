package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

public interface ISymbolVisitor {
    void visit(PointSymbol symbol);
    void visit(ImageSymbol symbol);
    void visit(UserSymbol symbol);
}
