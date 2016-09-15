package com.teamagam.gimelgimel.data.user.repository;

import rx.Observable;

public interface PreferencesProvider {
    Observable<String> getSenderId();
}
