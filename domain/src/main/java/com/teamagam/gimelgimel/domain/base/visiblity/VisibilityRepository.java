package com.teamagam.gimelgimel.domain.base.visiblity;

import rx.Observable;

public interface VisibilityRepository<T extends VisibilityChange> {

  Observable<T> getChangesObservable();

  void addChange(T change);
}
