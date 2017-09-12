package com.teamagam.gimelgimel.domain.dynamicLayers.details;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import io.reactivex.Observable;

@AutoFactory
public class SetSelectedDisplayedDynamicLayerDetails extends BaseSingleDataInteractor {

  private final SingleDisplayedItemRepository<DynamicLayerClickInfo>
      mDynamicLayerSingleDisplayedItemRepository;
  private final DynamicLayerToEntityMapper mMapper;
  private final String mEntityId;

  protected SetSelectedDisplayedDynamicLayerDetails(@Provided ThreadExecutor threadExecutor,
      @Provided
          SingleDisplayedItemRepository<DynamicLayerClickInfo> dynamicLayerSingleDisplayedItemRepository,
      @Provided DynamicLayerToEntityMapper dynamicLayerToEntityMapper,
      String entityId) {
    super(threadExecutor);
    mDynamicLayerSingleDisplayedItemRepository = dynamicLayerSingleDisplayedItemRepository;
    mMapper = dynamicLayerToEntityMapper;
    mEntityId = entityId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mEntityId), idObsv -> idObsv.map(mMapper::getByGeoEntity)
        .filter(x -> x != null).map(this::buildClickInfo)
        .doOnNext(mDynamicLayerSingleDisplayedItemRepository::setItem));
  }

  private DynamicLayerClickInfo buildClickInfo(DynamicLayer dynamicLayer) {
    DynamicEntity dynamicEntity = dynamicLayer.getEntityById(mEntityId);
    return new DynamicLayerClickInfo(dynamicLayer, dynamicEntity);
  }
}