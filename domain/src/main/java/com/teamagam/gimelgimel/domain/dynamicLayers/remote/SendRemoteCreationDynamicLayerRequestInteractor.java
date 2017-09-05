package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import io.reactivex.Observable;

@AutoFactory
public class SendRemoteCreationDynamicLayerRequestInteractor extends BaseSingleDataInteractor {

  private final DynamicLayerRemoteSourceHandler mDynamicLayerRemoteSourceHandler;
  private final String mName;

  protected SendRemoteCreationDynamicLayerRequestInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DynamicLayerRemoteSourceHandler dynamicLayerRemoteSourceHandler,
      String name) {
    super(threadExecutor);
    mDynamicLayerRemoteSourceHandler = dynamicLayerRemoteSourceHandler;
    mName = name;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mName),
        nameObservable -> nameObservable.doOnNext(this::sendDynamicLayerCreationRequest));
  }

  private void sendDynamicLayerCreationRequest(String name) {
    mDynamicLayerRemoteSourceHandler.createDynamicLayer(name, "");
  }
}
