package com.teamagam.gimelgimel.domain.messages.repository;

import java.util.Date;

import rx.Observable;

public interface UnreadMessagesCountRepository {

    Observable<Integer> getNumUnreadMessagesObservable();

    Observable<Date> getLastVisitTimestampObservable();

    void addNewUnreadMessage();

    void readAllUntil(Date date);
}
