package com.teamagam.gimelgimel.app.map.esri.graphic;


import android.content.Context;

import com.esri.core.symbol.Symbol;

public class EsriSymbolCreator {

    private Context mContext;

    public EsriSymbolCreator(Context context) {
        mContext = context;
    }

    public Symbol create(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
        Symbol specificSymbol = transform(ggSymbol);
        if (ggSymbol.isSelected()) {
            specificSymbol = select(ggSymbol, specificSymbol);
        }

        return specificSymbol;
    }

    private Symbol select(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol,
                          Symbol baseSymbol) {
        SelectionSymbolizerVisitor visitor = new SelectionSymbolizerVisitor(baseSymbol);
        ggSymbol.accept(visitor);

        return visitor.getEsriSymbol();
    }

    private Symbol transform(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
        EsriSymbolCreationVisitor visitor = new EsriSymbolCreationVisitor(mContext);
        ggSymbol.accept(visitor);

        return visitor.getEsriSymbol();
    }

}
