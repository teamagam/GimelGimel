package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PointSymbol extends BaseSymbol {

  private final String mIconId;
  private final String mTintColor;

  protected PointSymbol(boolean isSelected, String text, String iconId, String tintColor) {
    super(isSelected, text);
    mIconId = iconId;
    mTintColor = tintColor;
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }

  public String getIconId() {
    return mIconId;
  }

  public String getTintColor() {
    return mTintColor;
  }

  public static class PointSymbolBuilder {
    private boolean mIsSelected = false;
    private String mText = null;
    private String mIconId = null;
    private String mTintColor = null;

    public PointSymbolBuilder copy(PointSymbol symbol) {
      mIsSelected = symbol.isSelected();
      mText = symbol.getText();
      mIconId = symbol.getIconId();
      mTintColor = symbol.getTintColor();
      return this;
    }

    public PointSymbolBuilder setIsSelected(boolean isSelected) {
      mIsSelected = isSelected;
      return this;
    }

    public PointSymbolBuilder setText(String text) {
      mText = text;
      return this;
    }

    public PointSymbolBuilder setIconId(String iconId) {
      mIconId = iconId;
      return this;
    }

    public PointSymbolBuilder setTintColor(String tintColor) {
      mTintColor = tintColor;
      return this;
    }

    public PointSymbol build() {
      return new PointSymbol(mIsSelected, mText, mIconId, mTintColor);
    }
  }
}