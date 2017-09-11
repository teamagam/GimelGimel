package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.SetDisplayedDynamicLayerDetailsFactory;
import io.reactivex.Observable;

@AutoFactory
public class OnEntityClickedInteractor extends BaseSingleDataInteractor {

  private final SelectMessageByEntityInteractorFactory mSelectEntityInteractorFactory;
  private final SetDisplayedDynamicLayerDetailsFactory mSetDisplayedDynamicLayerDetailsFactory;
  private final String mEntityId;

  protected OnEntityClickedInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided SelectMessageByEntityInteractorFactory selectEntityInteractorFactory,
      @Provided
          com.teamagam.gimelgimel.domain.dynamicLayers.details.SetDisplayedDynamicLayerDetailsFactory setDisplayedDynamicLayerDetailsFactory,
      String entityId) {
    super(threadExecutor);
    mSelectEntityInteractorFactory = selectEntityInteractorFactory;
    mSetDisplayedDynamicLayerDetailsFactory = setDisplayedDynamicLayerDetailsFactory;
    mEntityId = entityId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mEntityId),
        idObs -> idObs.doOnNext(this::selectEntityAndMessage).doOnNext(this::selectDynamicEntity));
  }

  private void selectEntityAndMessage(String entityId) {
    mSelectEntityInteractorFactory.create(entityId).execute();
  }

  private void selectDynamicEntity(String entityId) {
    mSetDisplayedDynamicLayerDetailsFactory.create(entityId).execute();
  }
}
