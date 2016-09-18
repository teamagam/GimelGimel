package com.teamagam.gimelgimel.app.utils;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.data.user.repository.PreferencesProvider;

import rx.Observable;

public class AndroidPreferencesProvider implements PreferencesProvider {

    private PreferenceUtil mPreferences;

    public AndroidPreferencesProvider(PreferenceUtil preferences) {
        mPreferences = preferences;
    }

    @Override
    public Observable<String> getSenderId() {
        return Observable.just(mPreferences.getString(R.string.user_name_text_key));
    }
}
