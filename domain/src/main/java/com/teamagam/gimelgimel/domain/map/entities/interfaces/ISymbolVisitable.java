package com.teamagam.gimelgimel.domain.map.entities.interfaces;

public interface ISymbolVisitable {
    void accept(ISymbolVisitor visitor);
}
