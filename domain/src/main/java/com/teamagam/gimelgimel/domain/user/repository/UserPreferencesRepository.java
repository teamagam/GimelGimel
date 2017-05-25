package com.teamagam.gimelgimel.domain.user.repository;

import java.util.Set;

public interface UserPreferencesRepository {

  String getString(String key);

  boolean getBoolean(String key);

  float getFloat(String key);

  int getInt(String key);

  long getLong(String key);

  Set<String> getStringSet(String key);

  boolean contains(String key);

  void setPreference(String key, String value);

  void setPreference(String key, boolean value);

  void setPreference(String key, float value);

  void setPreference(String key, int value);

  void setPreference(String key, long value);

  void setPreference(String key, Set<String> values);
}
