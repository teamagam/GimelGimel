package com.teamagam.gimelgimel.domain.base.interactors;

import io.reactivex.observers.ResourceObserver;
import java.util.ArrayList;
import java.util.Collection;

abstract class BaseInteractor implements Interactor {

  private final Collection<ResourceObserver> mObservers;

  BaseInteractor() {
    mObservers = new ArrayList<>();
  }

  @Override
  public final void execute() {
    Iterable<SubscriptionRequest> subscriptionRequests = buildSubscriptionRequests();
    for (SubscriptionRequest se : subscriptionRequests) {
      subscribe(se);
    }
  }

  @Override
  public final void unsubscribe() {
    mObservers.stream()
        .filter(observer -> observer != null && !observer.isDisposed())
        .forEach(ResourceObserver::dispose);
  }

  protected abstract Iterable<SubscriptionRequest> buildSubscriptionRequests();

  private void subscribe(SubscriptionRequest se) {
    ResourceObserver observer = se.subscribe();
    mObservers.add(observer);
  }

  protected interface SubscriptionRequest {
    ResourceObserver subscribe();
  }
}
