package com.teamagam.gimelgimel.data.user.repository;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.util.Set;

@SuppressLint("UseSparseArrays")
public class UserPreferenceRepositoryImpl implements UserPreferencesRepository {

  private SharedPreferences mSharedPreferences;

  public UserPreferenceRepositoryImpl(SharedPreferences sharedPreferences) {
    mSharedPreferences = sharedPreferences;
  }

  @Override
  public String getString(String key) {
    throwExceptionIfNotExist(key);
    return mSharedPreferences.getString(key, null);
  }

  @Override
  public boolean getBoolean(String key) {
    throwExceptionIfNotExist(key);
    return mSharedPreferences.getBoolean(key, false);
  }

  @Override
  public float getFloat(String key) {
    throwExceptionIfNotExist(key);
    return mSharedPreferences.getFloat(key, 0f);
  }

  @Override
  public int getInt(String key) {
    throwExceptionIfNotExist(key);
    return mSharedPreferences.getInt(key, 0);
  }

  @Override
  public long getLong(String key) {
    throwExceptionIfNotExist(key);
    return mSharedPreferences.getLong(key, 0);
  }

  @Override
  public Set<String> getStringSet(String key) {
    throwExceptionIfNotExist(key);
    return mSharedPreferences.getStringSet(key, null);
  }

  @Override
  public boolean contains(String key) {
    return mSharedPreferences.contains(key);
  }

  @Override
  public void setPreference(String key, String value) {
    setPreference(key, (Object) value);
  }

  @Override
  public void setPreference(String key, boolean value) {
    setPreference(key, (Object) value);
  }

  @Override
  public void setPreference(String key, float value) {
    setPreference(key, (Object) value);
  }

  @Override
  public void setPreference(String key, int value) {
    setPreference(key, (Object) value);
  }

  @Override
  public void setPreference(String key, long value) {
    setPreference(key, (Object) value);
  }

  @Override
  public void setPreference(String key, Set<String> values) {
    setPreference(key, (Object) values);
  }

  private void setPreference(String key, Object value) {
    SharedPreferences.Editor editor = mSharedPreferences.edit();
    updateSharedPreferencesByObjectType(key, value, editor);
    editor.apply();
  }

  private void updateSharedPreferencesByObjectType(String key,
      Object value,
      SharedPreferences.Editor editor) {
    if (value instanceof Integer) {
      editor.putInt(key, (int) value);
    } else if (value instanceof Boolean) {
      editor.putBoolean(key, (boolean) value);
    } else if (value instanceof Float) {
      editor.putFloat(key, (float) value);
    } else if (value instanceof Long) {
      editor.putLong(key, (long) value);
    } else if (value instanceof String) {
      editor.putString(key, (String) value);
    } else if (value instanceof Set) {
      editor.putStringSet(key, (Set<String>) value);
    } else {
      throw new RuntimeException("Object is not supported by SharedPreferences!");
    }
  }

  private void throwExceptionIfNotExist(String key) {
    if (!mSharedPreferences.contains(key)) {
      throw new RuntimeException(
          String.format("Key '%s' does not exist in UserPreferencesRepository.", key));
    }
  }
}
