package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import java.util.Collections;
import rx.Observable;

@AutoFactory
public class OnVectorLayerListingClickInteractor extends BaseDataInteractor {

  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final VectorLayerExtentResolver mVectorLayerExtentResolver;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
  private final VectorLayerPresentation mVectorLayerPresentation;

  public OnVectorLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      @Provided VectorLayerExtentResolver vectorLayerExtentResolver,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      VectorLayerPresentation vectorLayerPresentation) {
    super(threadExecutor);
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mVectorLayerPresentation = vectorLayerPresentation;
    mVectorLayerExtentResolver = vectorLayerExtentResolver;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    SubscriptionRequest setVisibilityRequest =
        factory.create(Observable.just(mVectorLayerPresentation),
            vectorLayerVisibilityChangeObservable -> vectorLayerVisibilityChangeObservable.doOnNext(
                this::toggleVisibility).filter(this::isToShow).doOnNext(this::goToExtent));
    return Collections.singletonList(setVisibilityRequest);
  }

  private void toggleVisibility(VectorLayerPresentation vectorLayerPresentation) {
    VectorLayerVisibilityChange change =
        new VectorLayerVisibilityChange(vectorLayerPresentation.getId(),
            isToShow(vectorLayerPresentation));
    mVectorLayersVisibilityRepository.addChange(change);
  }

  private boolean isToShow(VectorLayerPresentation vlp) {
    return !vlp.isShown();
  }

  private void goToExtent(VectorLayerPresentation vlp) {
    Geometry extent = mVectorLayerExtentResolver.getExtent(vlp);
    mGoToLocationMapInteractorFactory.create(extent).execute();
  }
}
