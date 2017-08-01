package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import io.reactivex.Observable;

@AutoFactory
public class OnDynamicLayerListingClickInteractor extends BaseSingleDataInteractor {

  private final DynamicLayerVisibilityRepository mDynamicLayerVisibilityRepository;
  private final DynamicLayer mDynamicLayer;

  protected OnDynamicLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerVisibilityRepository dynamicLayerVisibilityRepository,
      DynamicLayer dynamicLayer) {
    super(threadExecutor);
    mDynamicLayerVisibilityRepository = dynamicLayerVisibilityRepository;
    mDynamicLayer = dynamicLayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayer),
        dlObservable -> dlObservable.map(this::getNewVisibilityState)
            .doOnNext(this::setNewVisibility));
  }

  private boolean getNewVisibilityState(DynamicLayer dynamicLayer) {

    return !mDynamicLayerVisibilityRepository.isVisible(dynamicLayer.getId());
  }

  private void setNewVisibility(Boolean newVisibilityState) {
    mDynamicLayerVisibilityRepository.addChange(
        new DynamicLayerVisibilityChange(newVisibilityState, mDynamicLayer.getId()));
  }
}
