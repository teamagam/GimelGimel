package com.teamagam.gimelgimel.domain.base.interactors;

import java.util.ArrayList;
import java.util.Collection;
import org.reactivestreams.Subscription;

abstract class BaseInteractor implements Interactor {

  private final Collection<Subscription> mSubscriptions;

  BaseInteractor() {
    mSubscriptions = new ArrayList<>();
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
    for (Subscription sub : mSubscriptions) {
      if (sub != null && !sub.isUnsubscribed()) {
        sub.unsubscribe();
      }
    }
  }

  protected abstract Iterable<SubscriptionRequest> buildSubscriptionRequests();

  private void subscribe(SubscriptionRequest se) {
    Subscription subscription = se.subscribe();
    mSubscriptions.add(subscription);
  }

  protected interface SubscriptionRequest {
    Subscription subscribe();
  }
}
