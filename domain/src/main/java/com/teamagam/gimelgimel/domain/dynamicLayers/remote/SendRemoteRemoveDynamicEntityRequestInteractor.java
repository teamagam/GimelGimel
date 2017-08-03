package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Observable;

@AutoFactory
public class SendRemoteRemoveDynamicEntityRequestInteractor extends BaseSingleDataInteractor {

  private final DynamicLayerRemoteSourceHandler mDynamicLayerRemoteSourceHandler;
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final RetryWithDelay mRetryWithDelay;
  private final String mDynamicLayerId;
  private final String mEntityId;

  public SendRemoteRemoveDynamicEntityRequestInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerRemoteSourceHandler dynamicLayerRemoteSourceHandler,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      @Provided RetryWithDelay retryWithDelay,
      String dynamicLayerId,
      String entityId) {
    super(threadExecutor);
    mDynamicLayerRemoteSourceHandler = dynamicLayerRemoteSourceHandler;
    mDynamicLayersRepository = dynamicLayersRepository;
    mRetryWithDelay = retryWithDelay;
    mDynamicLayerId = dynamicLayerId;
    mEntityId = entityId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayerId),
        dlIdObservable -> dlIdObservable.map(mDynamicLayersRepository::getById)
            .retryWhen(mRetryWithDelay)
            .doOnNext(dl -> mDynamicLayerRemoteSourceHandler.removeEntity(dl,
                dl.getEntityById(mEntityId))));
  }
}
