package com.teamagam.gimelgimel.domain.user.repository;

import rx.Observable;

public interface UserPreferencesRepository {
    Observable<String> getSenderId();
}
