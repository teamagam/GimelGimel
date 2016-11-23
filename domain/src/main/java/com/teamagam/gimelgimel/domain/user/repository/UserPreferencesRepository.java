package com.teamagam.gimelgimel.domain.user.repository;

import java.util.Map;

public interface UserPreferencesRepository {

    Map<String, Object> getAllPreferences();

    void setAllPreferences(Map<String, Object> preferences);

    void updatePreferences(Map<String, Object> preferences);

    <T> T getPreference(String key);

    void setPreference(String key, Object value);
}
