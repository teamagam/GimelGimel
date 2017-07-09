package com.teamagam.gimelgimel.domain.map.entities.symbols;

public abstract class BaseSymbol implements Symbol {

  private final boolean mIsSelected;
  private final String mText;

  BaseSymbol(boolean isSelected) {
    this(isSelected, null);
  }

  BaseSymbol(boolean isSelected, String text) {
    mIsSelected = isSelected;
    mText = text;
  }

  @Override
  public boolean isSelected() {
    return mIsSelected;
  }

  public String getText() {
    return mText;
  }

  public boolean hasText() {
    return mText != null;
  }
}