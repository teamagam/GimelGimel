package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.content.Context;
import android.graphics.Color;
import com.esri.arcgisruntime.symbology.CompositeSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EsriSymbolCreator {

  private static final int DEFAULT_TEXT_SIZE = 20;
  private static final int DEFAULT_TEXT_COLOR = Color.BLUE;

  private EsriSymbolCreationVisitor mSymbolCreationVisitor;
  private Context mContext;

  @Inject
  public EsriSymbolCreator(Context context, EsriSymbolCreationVisitor symbolCreationVisitor) {
    mContext = context;
    mSymbolCreationVisitor = symbolCreationVisitor;
  }

  public Symbol create(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
    Symbol specificSymbol = getEsriSymbol(ggSymbol);
    if (ggSymbol.isSelected()) {
      specificSymbol = selectEsriSymbol(ggSymbol, specificSymbol);
    }

    return specificSymbol;
  }

  private Symbol getEsriSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol symbol) {
    symbol.accept(mSymbolCreationVisitor);
    Symbol esriSymbol = mSymbolCreationVisitor.getEsriSymbol();

    if (symbol.hasText()) {
      esriSymbol = new CompositeSymbol(Arrays.asList(esriSymbol, createTextSymbol(symbol)));
    }

    return esriSymbol;
  }

  private Symbol selectEsriSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol,
      Symbol baseSymbol) {
    SelectionSymbolizerVisitor visitor = new SelectionSymbolizerVisitor(mContext, baseSymbol);
    ggSymbol.accept(visitor);

    return visitor.getEsriSymbol();
  }

  private TextSymbol createTextSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol symbol) {
    return new TextSymbol(DEFAULT_TEXT_SIZE, symbol.getText(), DEFAULT_TEXT_COLOR,
        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.MIDDLE);
  }
}
