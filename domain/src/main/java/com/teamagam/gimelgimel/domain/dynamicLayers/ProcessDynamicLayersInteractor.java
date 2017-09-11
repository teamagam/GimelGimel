package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.details.DynamicLayerToEntityMapper;
import javax.inject.Inject;

public class ProcessDynamicLayersInteractor extends BaseSingleDataInteractor {

  private final DynamicLayersRepository mDynamicLayersRepository;
  private final DynamicLayerToEntityMapper mDynamicLayerToEntityMapper;
  private final DynamicLayerVisibilityRepository mDynamicLayerVisibilityRepository;

  @Inject
  public ProcessDynamicLayersInteractor(ThreadExecutor threadExecutor,
      DynamicLayersRepository dynamicLayersRepository,
      DynamicLayerToEntityMapper dynamicLayerToEntityMapper,
      DynamicLayerVisibilityRepository dynamicLayerVisibilityRepository) {
    super(threadExecutor);
    mDynamicLayersRepository = dynamicLayersRepository;
    mDynamicLayerToEntityMapper = dynamicLayerToEntityMapper;
    mDynamicLayerVisibilityRepository = dynamicLayerVisibilityRepository;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(mDynamicLayersRepository.getObservable(),
        dlObservable -> dlObservable.doOnNext(this::mapEntitiesWithLayer)
            .doOnNext(this::setAsVisible));
  }

  private void mapEntitiesWithLayer(DynamicLayer dynamicLayer) {
    mDynamicLayerToEntityMapper.map(dynamicLayer);
  }

  private void setAsVisible(DynamicLayer dl) {
    mDynamicLayerVisibilityRepository.addChange(createVisibleChange(dl));
  }

  private DynamicLayerVisibilityChange createVisibleChange(DynamicLayer dl) {
    return new DynamicLayerVisibilityChange(true, dl.getId());
  }
}
