package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;

@AutoFactory
public class DisplayDynamicLayerDetailsInteractor extends BaseSingleDisplayInteractor {

  private final DynamicLayersRepository mDynamicLayersRepository;
  private final String mDynamicLayerId;
  private final Displayer mDisplayer;

  public DisplayDynamicLayerDetailsInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      Displayer displayer,
      String dynamicLayerId) {
    super(threadExecutor, postExecutionThread);
    mDynamicLayersRepository = dynamicLayersRepository;
    mDisplayer = displayer;
    mDynamicLayerId = dynamicLayerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(mDynamicLayersRepository.getObservable(),
        dlsObs -> dlsObs.filter(dl -> dl.getId().equals(mDynamicLayerId)), mDisplayer::display);
  }

  public interface Displayer {
    void display(DynamicLayer dynamicLayer);
  }
}
