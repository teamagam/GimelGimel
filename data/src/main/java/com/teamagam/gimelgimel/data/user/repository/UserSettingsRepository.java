package com.teamagam.gimelgimel.data.user.repository;

import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import rx.Observable;

public class UserSettingsRepository implements UserPreferencesRepository {

    private PreferencesProvider mProvider;

    public UserSettingsRepository(PreferencesProvider provider) {
        mProvider = provider;
    }

    @Override
    public Observable<String> getSenderId() {
        return mProvider.getSenderId();
    }
}
