package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Observable;

@AutoFactory
public class SendRemoteUpdateDescriptionDynamicLayerRequestInteractor
    extends BaseSingleDataInteractor {

  private final DynamicLayerRemoteSourceHandler mDynamicLayerRemoteSourceHandler;
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final String mDynamicLayerId;
  private final String mDescription;

  protected SendRemoteUpdateDescriptionDynamicLayerRequestInteractor(
      @Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerRemoteSourceHandler dynamicLayerRemoteSourceHandler,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      String description,
      String layerId) {
    super(threadExecutor);
    mDynamicLayerRemoteSourceHandler = dynamicLayerRemoteSourceHandler;
    mDynamicLayersRepository = dynamicLayersRepository;
    mDescription = description;
    mDynamicLayerId = layerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayerId),
        idObservable -> idObservable.doOnNext(this::createAndSendUpdatedDynamicLayer));
  }

  private void createAndSendUpdatedDynamicLayer(String layerId) {
    DynamicLayer dynamicLayer = mDynamicLayersRepository.getById(layerId);
    DynamicLayer updatedDynamicLayer =
        new DynamicLayer(dynamicLayer.getId(), dynamicLayer.getName(), mDescription, 0,
            dynamicLayer.getEntities());
    mDynamicLayerRemoteSourceHandler.updateDescription(updatedDynamicLayer);
  }
}
