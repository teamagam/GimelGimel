package com.teamagam.gimelgimel.data.user.repository;

public interface PreferencesProvider {
    String getSenderId();

    long getLatestMessageDate();

    void updateLatestMessageDate(long newSynchronizationDate);
}
