package com.teamagam.gimelgimel.domain.utils;

import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

public class MessagesUtil {

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;

    @Inject
    public MessagesUtil() {
    }

    public boolean isMessageFromSelf(Message message) {
        return message.getSenderId().equals(mUserPreferencesRepository.getPreference(
                Constants.USERNAME_PREFRENCE_KEY));
    }
}
