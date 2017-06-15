package com.teamagam.gimelgimel.domain.messages.repository;

import io.reactivex.Observable;
import java.util.Date;

public interface UnreadMessagesCountRepository {

  Observable<Integer> getNumUnreadMessagesObservable();

  Observable<Date> getLastVisitTimestampObservable();

  Date getLastVisitTimestamp();

  void addNewUnreadMessage(Date messageDate);

  void updateLastVisit(Date date);
}
