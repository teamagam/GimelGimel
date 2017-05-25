package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class SensorSymbol extends BaseSymbol {

  private final String mName;

  public SensorSymbol(boolean isSelected, String name) {
    super(isSelected);
    mName = name;
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }

  public String getName() {
    return mName;
  }
}