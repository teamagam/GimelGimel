package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import io.reactivex.Observable;

@AutoFactory
public class OnDynamicLayerListingClickInteractor extends BaseSingleDataInteractor {

  private final DynamicLayerVisibilityRepository mDynamicLayerVisibilityRepository;
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final DynamicLayerGoToGeometryCalculator mDynamicLayerGoToGeometryCalculator;
  private final String mDynamicLayerId;

  protected OnDynamicLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerVisibilityRepository dynamicLayerVisibilityRepository,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      String dynamicLayerId) {
    super(threadExecutor);
    mDynamicLayerVisibilityRepository = dynamicLayerVisibilityRepository;
    mDynamicLayersRepository = dynamicLayersRepository;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mDynamicLayerGoToGeometryCalculator = new DynamicLayerGoToGeometryCalculator();
    mDynamicLayerId = dynamicLayerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayerId),
        dynamicLayerIdObservable -> dynamicLayerIdObservable.map(mDynamicLayersRepository::getById)
            .map(this::toPresentation)
            .doOnNext(this::setNewVisibility)
            .filter(DynamicLayerPresentation::isShown)
            .filter(this::hasEntities)
            .doOnNext(this::goToExtent));
  }

  private DynamicLayerPresentation toPresentation(DynamicLayer dynamicLayer) {
    boolean toggledVisibility = !mDynamicLayerVisibilityRepository.isVisible(dynamicLayer.getId());
    return new DynamicLayerPresentation(dynamicLayer, toggledVisibility);
  }

  private void setNewVisibility(DynamicLayerPresentation dlp) {
    mDynamicLayerVisibilityRepository.addChange(
        new DynamicLayerVisibilityChange(dlp.isShown(), dlp.getId()));
  }

  private boolean hasEntities(DynamicLayerPresentation dlp) {
    return !dlp.getEntities().isEmpty();
  }

  private void goToExtent(DynamicLayer dl) {
    mGoToLocationMapInteractorFactory.create(mDynamicLayerGoToGeometryCalculator.getGoToTarget(dl))
        .execute();
  }
}
