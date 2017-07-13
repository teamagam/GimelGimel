package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitable;

public interface Symbol extends ISymbolVisitable {

  boolean isSelected();

  boolean hasText();

  String getText();
}
