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
    Iterable<SubscriptionRequest> requests = buildSubscriptionRequests();
    for (SubscriptionRequest request : requests) {
      subscribe(request);
    }
  }

  @Override
  public final void unsubscribe() {
    for (ResourceObserver observer : mObservers) {
      if (observer != null && !observer.isDisposed()) {
        observer.dispose();
      }
    }
  }

  protected abstract Iterable<SubscriptionRequest> buildSubscriptionRequests();

  private void subscribe(SubscriptionRequest request) {
    ResourceObserver observer = request.subscribe();
    mObservers.add(observer);
  }

  protected interface SubscriptionRequest {
    ResourceObserver subscribe();
  }
}
