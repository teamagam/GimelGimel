package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PolylineSymbol extends BaseSymbol {

  private final String mBorderColor;
  private final String mBorderStyle;

  protected PolylineSymbol(boolean isSelected,
      String text,
      String borderColor,
      String borderStyle) {
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

  public static class PolylineSymbolBuilder {
    private boolean mIsSelected = false;
    private String mText = null;
    private String mBorderColor = null;
    private String mBorderStyle = null;

    public PolylineSymbolBuilder copy(PolylineSymbol symbol) {
      mIsSelected = symbol.isSelected();
      mText = symbol.getText();
      mBorderColor = symbol.getBorderColor();
      mBorderStyle = symbol.getBorderStyle();
      return this;
    }

    public PolylineSymbolBuilder setIsSelected(boolean isSelected) {
      mIsSelected = isSelected;
      return this;
    }

    public PolylineSymbolBuilder setText(String text) {
      mText = text;
      return this;
    }

    public PolylineSymbolBuilder setBorderColor(String borderColor) {
      mBorderColor = borderColor;
      return this;
    }

    public PolylineSymbolBuilder setBorderStyle(String borderStyle) {
      mBorderStyle = borderStyle;
      return this;
    }

    public PolylineSymbol build() {
      return new PolylineSymbol(mIsSelected, mText, mBorderColor, mBorderStyle);
    }
  }
}
