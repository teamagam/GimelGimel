package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import io.reactivex.functions.Consumer;

@AutoFactory
public class ActOnFirstLocationInteractor extends BaseSingleDisplayInteractor {

  private final LocationRepository mLocationRepository;
  private final Consumer<LocationSample> mAction;

  protected ActOnFirstLocationInteractor(
      @Provided
          ThreadExecutor threadExecutor,
      @Provided
          PostExecutionThread postExecutionThread,
      @Provided
          LocationRepository locationRepository, Consumer <LocationSample> action) {
    super(threadExecutor, postExecutionThread);
    mLocationRepository = locationRepository;
    mAction = action;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(
      DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.createSimple(mLocationRepository.getLocationObservable().take(1), mAction);
  }
}
