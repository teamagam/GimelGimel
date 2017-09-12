package com.teamagam.gimelgimel.domain.dynamicLayers.details;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import io.reactivex.Observable;
import javax.inject.Inject;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

public class ClearDisplayedDynamicLayerDetailsInteractor extends BaseSingleDataInteractor {

  private final SingleDisplayedItemRepository<DynamicLayer> mRepository;

  @Inject
  protected ClearDisplayedDynamicLayerDetailsInteractor(ThreadExecutor threadExecutor,
      SingleDisplayedItemRepository<DynamicLayer> dynamicLayerSingleDisplayedItemRepository) {
    super(threadExecutor);
    mRepository = dynamicLayerSingleDisplayedItemRepository;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(SIGNAL), signal -> signal.doOnNext(this::clear));
  }

  private void clear(Object o) {
    mRepository.clear();
  }
}
