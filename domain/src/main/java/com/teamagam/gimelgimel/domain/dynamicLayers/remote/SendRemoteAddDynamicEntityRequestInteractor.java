package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;

@AutoFactory
public class SendRemoteAddDynamicEntityRequestInteractor extends BaseSingleDataInteractor {

  private final DynamicLayerRemoteSourceHandler mDynamicLayerRemoteSourceHandler;
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final RetryWithDelay mRetryWithDelay;
  private final GeoEntity mEntity;

  public SendRemoteAddDynamicEntityRequestInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerRemoteSourceHandler dynamicLayerRemoteSourceHandler,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      @Provided RetryWithDelay retryWithDelay,
      GeoEntity entity) {
    super(threadExecutor);
    mDynamicLayerRemoteSourceHandler = dynamicLayerRemoteSourceHandler;
    mDynamicLayersRepository = dynamicLayersRepository;
    mRetryWithDelay = retryWithDelay;
    mEntity = entity;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayersRepository),
        repoObservable -> repoObservable.flatMap(DynamicLayersRepository::getObservable)
            .retryWhen(mRetryWithDelay)
            .take(1)
            .doOnNext(dl -> mDynamicLayerRemoteSourceHandler.addEntity(dl, mEntity)));
  }
}
