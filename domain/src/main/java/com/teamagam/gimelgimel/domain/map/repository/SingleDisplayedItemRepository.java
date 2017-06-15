package com.teamagam.gimelgimel.domain.map.repository;

import io.reactivex.Observable;

public interface SingleDisplayedItemRepository<T> {
  Observable<DisplayEvent> getDisplayEventsObservable();

  T getItem();

  void setItem(T item);

  void clear();

  enum DisplayEvent {
    DISPLAY,
    HIDE
  }
}
