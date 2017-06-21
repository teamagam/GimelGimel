package com.teamagam.gimelgimel.domain.utils;

import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import static com.teamagam.gimelgimel.domain.config.Constants.NOTIFICATION_MODE_PREF_KEY;
import static com.teamagam.gimelgimel.domain.config.Constants.USE_UTM_PREF_KEY;

public class PreferencesUtils {

  UserPreferencesRepository mUserPreferencesRepository;

  public PreferencesUtils(UserPreferencesRepository userPreferencesRepository) {
    mUserPreferencesRepository = userPreferencesRepository;
    setDefaults();
  }

  public boolean isMessageFromSelf(String senderId) {
    return senderId.equals(mUserPreferencesRepository.getString(Constants.USERNAME_PREF_KEY));
  }

  public boolean shouldUseUtm() {
    boolean shouldUseUtm = false;
    if (mUserPreferencesRepository.contains(USE_UTM_PREF_KEY)) {
      shouldUseUtm = mUserPreferencesRepository.getBoolean(USE_UTM_PREF_KEY);
    }
    return shouldUseUtm;
  }

  public void toggleCoordinateSystemPrefs() {
    mUserPreferencesRepository.setPreference(USE_UTM_PREF_KEY, !shouldUseUtm());
  }

  public boolean isOnlyAlertsMode() {
    boolean isOnlyAlertsMode = false;
    if (mUserPreferencesRepository.contains(NOTIFICATION_MODE_PREF_KEY)) {
      isOnlyAlertsMode = mUserPreferencesRepository.getBoolean(NOTIFICATION_MODE_PREF_KEY);
    }
    return isOnlyAlertsMode;
  }

  public void toggleNotificationMode() {
    mUserPreferencesRepository.setPreference(NOTIFICATION_MODE_PREF_KEY, !isOnlyAlertsMode());
  }

  private void setDefaults() {
    if (!mUserPreferencesRepository.contains(Constants.USERNAME_PREF_KEY)) {
      mUserPreferencesRepository.setPreference(Constants.USERNAME_PREF_KEY,
          Constants.DEFAULT_USERNAME);
    }
  }
}
