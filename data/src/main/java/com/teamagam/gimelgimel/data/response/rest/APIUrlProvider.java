package com.teamagam.gimelgimel.data.response.rest;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import javax.inject.Inject;

public class APIUrlProvider {

  private final UserPreferencesRepository mUserPreferencesRepository;

  @Inject
  public APIUrlProvider(UserPreferencesRepository userPreferencesRepository) {
    mUserPreferencesRepository = userPreferencesRepository;
  }

  String getMessagingServerUrl() {
    if (mUserPreferencesRepository.contains(Constants.MESSAGING_SERVER_PREF_KEY)) {
      return mUserPreferencesRepository.getString(Constants.MESSAGING_SERVER_PREF_KEY);
    } else {
      return Constants.MESSAGING_SERVER_DEFAULT;
    }
  }
}