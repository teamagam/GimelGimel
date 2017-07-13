package com.teamagam.gimelgimel.domain.map.entities.symbols;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;

public class UserSymbol extends BaseSymbol {

  private final String mUserName;
  private final boolean mIsActive;

  private UserSymbol(boolean isSelected, String userName, boolean isActive) {
    super(isSelected, null);
    mUserName = userName;
    mIsActive = isActive;
  }

  public static UserSymbol createActive(String user, boolean isSelected) {
    return new UserSymbol(isSelected, user, true);
  }

  public static UserSymbol createStale(String user, boolean isSelected) {
    return new UserSymbol(isSelected, user, false);
  }

  public String getUserName() {
    return mUserName;
  }

  public boolean isActive() {
    return mIsActive;
  }

  @Override
  public void accept(ISymbolVisitor visitor) {
    visitor.visit(this);
  }
}