package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PolylineSymbol extends BaseSymbol {

  private final String mBorderColor;
  private final String mBorderStyle;

  public PolylineSymbol(boolean isSelected, String borderStyle, String borderColor) {
    this(isSelected, null, borderColor, borderStyle);
  }

  public PolylineSymbol(boolean isSelected, String text, String borderColor, String borderStyle) {
    super(isSelected, text);
    mBorderColor = borderColor;
    mBorderStyle = borderStyle;
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }

  public String getBorderColor() {
    return mBorderColor;
  }

  public String getBorderStyle() {
    return mBorderStyle;
  }
}
