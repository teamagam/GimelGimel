package com.teamagam.gimelgimel.app.utils;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.data.user.repository.PreferencesProvider;

public class AndroidPreferencesProvider implements PreferencesProvider {

    private PreferenceUtil mPreferences;

    public AndroidPreferencesProvider(PreferenceUtil preferences) {
        mPreferences = preferences;
    }

    @Override
    public String getSenderId() {
        return mPreferences.getString(R.string.user_name_text_key);
    }

    @Override
    public long getLatestMessageDate() {
        return mPreferences.getLong(R.string.pref_latest_received_message_date_in_ms, 0);
    }

    @Override
    public void updateLatestMessageDate(long newSynchronizationDate) {
        mPreferences.commitLong(R.string.pref_latest_received_message_date_in_ms,
                newSynchronizationDate);
    }
}
