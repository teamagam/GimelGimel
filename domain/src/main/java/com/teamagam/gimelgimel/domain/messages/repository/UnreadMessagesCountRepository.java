package com.teamagam.gimelgimel.domain.messages.repository;

import java.util.Date;
import io.reactivex.Observable;

public interface UnreadMessagesCountRepository {

  Observable<Integer> getNumUnreadMessagesObservable();

  Observable<Date> getLastVisitTimestampObservable();

  Date getLastVisitTimestamp();

  void addNewUnreadMessage(Date messageDate);

  void updateLastVisit(Date date);
}
