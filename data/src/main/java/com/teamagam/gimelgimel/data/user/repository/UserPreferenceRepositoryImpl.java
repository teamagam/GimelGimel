package com.teamagam.gimelgimel.data.user.repository;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class UserPreferenceRepositoryImpl implements UserPreferencesRepository,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mSharedPreferences;
    private Map<String, Object> mPreferences;

    public UserPreferenceRepositoryImpl(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mPreferences = new HashMap<>(mSharedPreferences.getAll());

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
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
        updateSharedPreferences(preferences);

        mPreferences.putAll(preferences);
    }

    @Override
    public <T> T getPreference(String key) {
        return (T) mPreferences.get(key);
    }

    @Override
    public void setPreference(String key, Object value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        updateSharedPreferenceByObject(key, value, editor);

        editor.apply();

        mPreferences.put(key, value);
    }

    /**
     * A method that listens to any changes in the SharedPreference object.
     * On any change, the method will update the preference of the repository
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mPreferences.put(key, sharedPreferences.getAll().get(key));
    }

    private void updateSharedPreferences(Map<String, Object> preferences) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (Map.Entry<String, Object> entry : preferences.entrySet()) {
            updateSharedPreferenceByObject(entry.getKey(), entry.getValue(), editor);
        }

        editor.apply();
    }

    private void updateSharedPreferenceByObject(String key, Object value,
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
        } else {
            throw new RuntimeException("Object is not supported by SharedPreferences!");
        }
    }
}
