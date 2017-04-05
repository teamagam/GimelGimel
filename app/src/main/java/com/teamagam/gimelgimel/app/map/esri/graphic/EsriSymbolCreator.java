package com.teamagam.gimelgimel.app.map.esri.graphic;


import com.esri.core.symbol.Symbol;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EsriSymbolCreator {

    private EsriSymbolCreationVisitor mSymbolCreationVisitor;

    @Inject
    public EsriSymbolCreator(EsriSymbolCreationVisitor symbolCreationVisitor) {
        mSymbolCreationVisitor = symbolCreationVisitor;
    }

    public Symbol create(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
        Symbol specificSymbol = getEsriSymbol(ggSymbol);
        if (ggSymbol.isSelected()) {
            specificSymbol = selectEsriSymbol(ggSymbol, specificSymbol);
        }

        return specificSymbol;
    }

    private Symbol getEsriSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
        ggSymbol.accept(mSymbolCreationVisitor);

        return mSymbolCreationVisitor.getEsriSymbol();
    }

    private Symbol selectEsriSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol,
                                    Symbol baseSymbol) {
        SelectionSymbolizerVisitor visitor = new SelectionSymbolizerVisitor(baseSymbol);
        ggSymbol.accept(visitor);

        return visitor.getEsriSymbol();
    }
}
