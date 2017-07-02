package com.teamagam.gimelgimel.domain.utils;

import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import static com.teamagam.gimelgimel.domain.config.Constants.USE_UTM_PREF_KEY;

public class PreferencesUtils {

  UserPreferencesRepository mUserPreferencesRepository;

  public PreferencesUtils(UserPreferencesRepository userPreferencesRepository) {
    mUserPreferencesRepository = userPreferencesRepository;
    setDefaults();
  }

  public boolean isMessageFromSelf(String senderId) {
    return senderId.equals(mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY));
  }

  public boolean shouldUseUtm() {
    if (mUserPreferencesRepository.contains(USE_UTM_PREF_KEY)) {
      return mUserPreferencesRepository.getBoolean(USE_UTM_PREF_KEY);
    }
    return false;
  }

  public void toggleCoordinateSystemPrefs() {
    mUserPreferencesRepository.setPreference(USE_UTM_PREF_KEY, !shouldUseUtm());
  }

  public boolean isOnlyAlertsMode() {
    if (mUserPreferencesRepository.contains(Constants.NOTIFICATION_ALERT_ONLY_PREF_KEY)) {
      return mUserPreferencesRepository.getBoolean(Constants.NOTIFICATION_ALERT_ONLY_PREF_KEY);
    }
    return false;
  }

  private void setDefaults() {
    if (!mUserPreferencesRepository.contains(Constants.USERNAME_PREFERENCE_KEY)) {
      mUserPreferencesRepository.setPreference(Constants.USERNAME_PREFERENCE_KEY,
          Constants.DEFAULT_USERNAME);
    }
  }
}
