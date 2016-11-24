package com.teamagam.gimelgimel.data.user.userManagment;

public interface PreferencesProvider {
    String getSenderId();

    long getLatestMessageDate();

    void updateLatestMessageDate(long newSynchronizationDate);
}
