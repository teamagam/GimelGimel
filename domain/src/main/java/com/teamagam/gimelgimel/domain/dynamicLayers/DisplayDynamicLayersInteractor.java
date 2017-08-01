package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;

@AutoFactory
public class DisplayDynamicLayersInteractor extends BaseSingleDisplayInteractor {
  private final DynamicLayersRepository mDynamicLayersRepository;
  private final DynamicLayerVisibilityRepository mDynamicLayerVisibilityRepository;
  private final Displayer mDisplayer;

  public DisplayDynamicLayersInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      @Provided DynamicLayerVisibilityRepository dynamicLayerVisibilityRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mDynamicLayersRepository = dynamicLayersRepository;
    mDynamicLayerVisibilityRepository = dynamicLayerVisibilityRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.createSimple(
        mDynamicLayerVisibilityRepository.getChangesObservable().map(this::toPresentation),
        mDisplayer::display);
  }

  private DynamicLayerPresentation toPresentation(DynamicLayerVisibilityChange change) {
    return new DynamicLayerPresentation(getLayer(change.getId()), change.isVisible());
  }

  private DynamicLayer getLayer(String layerId) {
    return mDynamicLayersRepository.getById(layerId);
  }

  public interface Displayer {
    void display(DynamicLayerPresentation dl);
  }
}
