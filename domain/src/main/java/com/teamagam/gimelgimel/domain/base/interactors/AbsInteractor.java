package com.teamagam.gimelgimel.domain.base.interactors;

import io.reactivex.Observable;
import io.reactivex.observers.ResourceObserver;

abstract class AbsInteractor<T> implements Interactor {

  private ResourceObserver<T> mObserver;

  public final void execute() {
    mObserver = buildObservable().subscribeWith(getObserver());
  }

  public final void unsubscribe() {
    if (!mObserver.isDisposed()) {
      mObserver.dispose();
    }
  }

  protected abstract Observable<T> buildObservable();

  protected abstract ResourceObserver<T> getObserver();
}
