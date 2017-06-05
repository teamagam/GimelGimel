package com.teamagam.gimelgimel.domain.base.interactors;

import io.reactivex.Observable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

abstract class AbsInteractor<T> implements Interactor {

  private Subscription mSubscription;

  public final void execute() {
    this.mSubscription = buildObservable().subscribe(getSubscriber());
  }

  public final void unsubscribe() {
    if (!mSubscription.isUnsubscribed()) {
      mSubscription.unsubscribe();
    }
  }

  protected abstract Observable<T> buildObservable();

  protected abstract Subscriber<T> getSubscriber();
}
