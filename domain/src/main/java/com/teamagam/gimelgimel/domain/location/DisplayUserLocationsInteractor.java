package com.teamagam.gimelgimel.domain.location;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

@AutoFactory
public class DisplayUserLocationsInteractor extends BaseSingleDisplayInteractor {

  private final UsersLocationRepository mUsersLocationRepository;
  private final Displayer mDisplayer;

  public DisplayUserLocationsInteractor(
      @Provided
          ThreadExecutor threadExecutor,
      @Provided
          PostExecutionThread postExecutionThread,
      @Provided
          UsersLocationRepository usersLocationRepository, Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mUsersLocationRepository = usersLocationRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(
      DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(createIntervalObservable(), observable -> observable.flatMapIterable(
        intervalSignaler -> mUsersLocationRepository.getLastLocations()), this::display);
  }

  private void display(UserLocation userLocation) {
    if (userLocation.isActive()) {
      mDisplayer.displayActive(userLocation);
    } else if (userLocation.isStale()) {
      mDisplayer.displayStale(userLocation);
    } else {
      mDisplayer.displayIrrelevant(userLocation);
    }
  }

  private Observable<Long> createIntervalObservable() {
    return Observable.interval(Constants.USERS_LOCATION_REFRESH_FREQUENCY_MS, TimeUnit.MILLISECONDS)
        .startWith((long) -1);
  }

  public interface Displayer {
    void displayActive(UserLocation userLocation);

    void displayStale(UserLocation userLocation);

    void displayIrrelevant(UserLocation userLocation);
  }
}
