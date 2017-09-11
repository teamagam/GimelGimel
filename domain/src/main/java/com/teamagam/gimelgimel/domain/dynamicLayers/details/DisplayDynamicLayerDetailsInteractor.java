package com.teamagam.gimelgimel.domain.dynamicLayers.details;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import io.reactivex.Observable;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

@AutoFactory
public class DisplayDynamicLayerDetailsInteractor extends BaseSingleDisplayInteractor {

  private final SingleDisplayedItemRepository<DynamicLayer>
      mDynamicLayerSingleDisplayedItemRepository;
  private final Displayer mDisplayer;

  public DisplayDynamicLayerDetailsInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided
          SingleDisplayedItemRepository<DynamicLayer> dynamicLayerSingleDisplayedItemRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mDynamicLayerSingleDisplayedItemRepository = dynamicLayerSingleDisplayedItemRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(Observable.just(SIGNAL),
        o -> o.flatMap(x -> mDynamicLayerSingleDisplayedItemRepository.getDisplayEventsObservable())
            .filter(displayEvent -> displayEvent.equals(
                SingleDisplayedItemRepository.DisplayEvent.DISPLAY))
            .map(de -> mDynamicLayerSingleDisplayedItemRepository.getItem()), mDisplayer::display);
  }

  public interface Displayer {
    void display(DynamicLayer dynamicLayer);
  }
}
