package com.teamagam.gimelgimel.domain.user;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import java.util.Arrays;
import rx.Observable;

@AutoFactory
public class OnUserListingClickedInteractor extends BaseDataInteractor {

  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final UserLocation mUserLocation;

  public OnUserListingClickedInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      UserLocation userLocation) {
    super(threadExecutor);
    mUserLocation = userLocation;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Arrays.asList(factory.create(Observable.just(mUserLocation),
        userLocationObservable -> userLocationObservable.doOnNext(this::goToUserLocationOnMap)));
  }

  private void goToUserLocationOnMap(UserLocation userLocation) {
    mGoToLocationMapInteractorFactory.create(userLocation.getLocationSample().getLocation())
        .execute();
  }
}