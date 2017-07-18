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
public class DisplayDynamicLayersInteractor extends BaseSingleDisplayInteractor {
  private DynamicLayersRepository mDynamicLayersRepository;
  private Displayer mDisplayer;

  public DisplayDynamicLayersInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mDynamicLayersRepository = dynamicLayersRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.createSimple(mDynamicLayersRepository.getObservable(), mDisplayer::display);
  }

  public interface Displayer {
    void display(DynamicLayer dl);
  }
}
