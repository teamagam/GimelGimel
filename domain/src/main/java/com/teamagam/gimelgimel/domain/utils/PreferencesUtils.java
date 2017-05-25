package com.teamagam.gimelgimel.domain.utils;

import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import static com.teamagam.gimelgimel.domain.config.Constants.USE_UTM_PREF_KEY;

public class PreferencesUtils {

  UserPreferencesRepository mUserPreferencesRepository;

  public PreferencesUtils(UserPreferencesRepository userPreferencesRepository) {
    mUserPreferencesRepository = userPreferencesRepository;
    setDefaults();
  }

  public boolean isMessageFromSelf(Message message) {
    return message.getSenderId()
        .equals(mUserPreferencesRepository.getString(Constants.USERNAME_PREFERENCE_KEY));
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

  private void setDefaults() {
    if (!mUserPreferencesRepository.contains(Constants.USERNAME_PREFERENCE_KEY)) {
      mUserPreferencesRepository.setPreference(Constants.USERNAME_PREFERENCE_KEY,
          Constants.DEFAULT_USERNAME);
    }
  }
}
