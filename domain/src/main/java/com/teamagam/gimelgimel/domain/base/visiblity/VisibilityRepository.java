package com.teamagam.gimelgimel.domain.base.visiblity;

import io.reactivex.Observable;

public interface VisibilityRepository<T extends VisibilityChange> {

  Observable<T> getChangesObservable();

  void addChange(T change);

  boolean isVisible(String id);
}
