package com.teamagam.gimelgimel.app.common.utils;

import java.util.HashMap;
import java.util.Map;

public class IntegerIdGenerator {

  private Map<Integer, String> mIntToStringMap;

  public IntegerIdGenerator() {
    mIntToStringMap = new HashMap<>();
  }

  public boolean contains(String stringId) {
    return mIntToStringMap.containsKey(stringId.hashCode());
  }

  public int get(String stringId) {
    int intId = stringId.hashCode();
    if (!mIntToStringMap.containsKey(intId)) {
      throw new RuntimeException("Mapper does not contain ID: " + stringId);
    }
    return intId;
  }

  public int generate(String stringId) {
    int intId = stringId.hashCode();
    mIntToStringMap.put(intId, stringId);
    return intId;
  }

  public String get(int intId) {
    return mIntToStringMap.get(intId);
  }
}
