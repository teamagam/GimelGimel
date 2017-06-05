package com.teamagam.gimelgimel.domain.map.repository;

import io.reactivex.Observable;

public interface SingleDisplayedItemRepository<T> {
  Observable<DisplayEvent> getDisplayEventsObservable();

  T getCurrentDisplayedItem();

  void setCurrentDisplayedItem(T item);

  enum DisplayEvent {
    DISPLAY,
    HIDE
  }
}
