package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import java.util.Collections;
import rx.Observable;

@AutoFactory
public class OnVectorLayerListingClickInteractor extends BaseDataInteractor {

  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final VectorLayerPresentation mVectorLayerPresentation;

  public OnVectorLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      VectorLayerPresentation vectorLayerPresentation) {
    super(threadExecutor);
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mVectorLayerPresentation = vectorLayerPresentation;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    SubscriptionRequest setVisibilityRequest =
        factory.create(Observable.just(mVectorLayerPresentation),
            vectorLayerVisibilityChangeObservable -> vectorLayerVisibilityChangeObservable.doOnNext(
                this::toggleVisibility));
    return Collections.singletonList(setVisibilityRequest);
  }

  private void toggleVisibility(VectorLayerPresentation vectorLayerPresentation) {
    VectorLayerVisibilityChange change =
        new VectorLayerVisibilityChange(vectorLayerPresentation.getId(),
            !vectorLayerPresentation.isShown());
    mVectorLayersVisibilityRepository.addChange(change);
  }
}
