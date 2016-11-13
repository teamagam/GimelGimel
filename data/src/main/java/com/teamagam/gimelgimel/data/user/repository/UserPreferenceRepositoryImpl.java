package com.teamagam.gimelgimel.data.user.repository;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class UserPreferenceRepositoryImpl implements UserPreferencesRepository {

    private SharedPreferences mSharedPreferences;
    private Map<String, Object> mPreferences;

    public UserPreferenceRepositoryImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mPreferences = new HashMap<>(mSharedPreferences.getAll());
    }

    @Override
    public Map<String, Object> getAllPreferences() {
        return new HashMap<>(mPreferences);
    }

    @Override
    public void setAllPreferences(Map<String, Object> preferences) {
        mPreferences = new HashMap<>(preferences);
    }

    @Override
    public void updatePreferences(Map<String, Object> preferences) {
        mPreferences.putAll(preferences);
    }

    @Override
    public Object getPreference(String key) {
        Object result = mPreferences.get(key);

        if (result == null) {
            return loadPreference(key);
        }

        return null;
    }

    @Override
    public void setPreference(String key, Object value) {
        //mSharedPreferences.edit().
    }

    private Object loadPreference(String key) {
        //mSharedPreferences.edit().

        return loadDefaultValue(key);
    }

    private Object loadDefaultValue(String key) {
        return null;
    }
}
