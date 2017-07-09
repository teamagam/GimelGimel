package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PolygonSymbol extends PolylineSymbol {

  private final String mFillColor;

  public PolygonSymbol(boolean isSelected,
      String borderStyle,
      String borderColor,
      String fillColor) {
    super(isSelected, borderStyle, borderColor);
    mFillColor = fillColor;
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }

  public String getFillColor() {
    return mFillColor;
  }
}