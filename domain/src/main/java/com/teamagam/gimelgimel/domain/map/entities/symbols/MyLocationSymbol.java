package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class MyLocationSymbol extends BaseSymbol {

  public MyLocationSymbol(boolean isSelected) {
    super(isSelected);
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }
}