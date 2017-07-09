package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class PointSymbol extends BaseSymbol {

  private final String mIconId;
  private final String mTintColor;

  public PointSymbol(boolean isSelected, String iconId, String tintColor) {
    super(isSelected);
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
}