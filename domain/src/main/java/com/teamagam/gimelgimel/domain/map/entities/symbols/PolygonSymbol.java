package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PolygonSymbol extends PolylineSymbol {

  private final String mFillColor;

  protected PolygonSymbol(boolean isSelected, String text,
      String borderStyle,
      String borderColor,
      String fillColor) {
    super(isSelected, text, borderColor, borderStyle);
    mFillColor = fillColor;
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }

  public String getFillColor() {
    return mFillColor;
  }

  public static class PolygonSymbolBuilder {
    private boolean mIsSelected = false;
    private String mText = null;
    private String mBorderStyle = null;
    private String mBorderColor = null;
    private String mFillColor = null;

    public PolygonSymbolBuilder copy(PolygonSymbol symbol) {
      mIsSelected = symbol.isSelected();
      mText = symbol.getText();
      mBorderStyle = symbol.getBorderStyle();
      mBorderColor = symbol.getBorderColor();
      mFillColor = symbol.getFillColor();
      return this;
    }

    public PolygonSymbolBuilder setIsSelected(boolean isSelected) {
      mIsSelected = isSelected;
      return this;
    }

    public PolygonSymbolBuilder setText(String text) {
      mText = text;
      return this;
    }

    public PolygonSymbolBuilder setBorderStyle(String borderStyle) {
      mBorderStyle = borderStyle;
      return this;
    }

    public PolygonSymbolBuilder setBorderColor(String borderColor) {
      mBorderColor = borderColor;
      return this;
    }

    public PolygonSymbolBuilder setFillColor(String fillColor) {
      mFillColor = fillColor;
      return this;
    }

    public PolygonSymbol build() {
      return new PolygonSymbol(mIsSelected, mText, mBorderStyle, mBorderColor, mFillColor);
    }
  }
}