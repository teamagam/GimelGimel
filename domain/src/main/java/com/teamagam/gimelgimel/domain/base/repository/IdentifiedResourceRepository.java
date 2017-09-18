package com.teamagam.gimelgimel.domain.base.repository;

import io.reactivex.Observable;

public interface IdentifiedResourceRepository<T extends IdentifiedData> {

  void put(T resource);

  T getById(String id);

  boolean contains(String id);

  Observable<T> getObservable();
}
