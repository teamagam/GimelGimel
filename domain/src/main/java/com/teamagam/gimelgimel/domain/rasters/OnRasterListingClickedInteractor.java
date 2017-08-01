package com.teamagam.gimelgimel.domain.rasters;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import io.reactivex.Observable;
import java.util.Collections;

@AutoFactory
public class OnRasterListingClickedInteractor extends BaseDataInteractor {

  private final IntermediateRasterVisibilityRepository mVisibilityRepository;
  private final IntermediateRasterPresentation mIntermediateRasterPresentation;
  private final IntermediateRasterExtentResolver mIntermediateRasterExtentResolver;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;

  public OnRasterListingClickedInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided IntermediateRasterVisibilityRepository visibilityRepository,
      @Provided IntermediateRasterExtentResolver intermediateRasterExtentResolver,
      @Provided
          com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      IntermediateRasterPresentation intermediateRasterPresentation) {
    super(threadExecutor);
    mVisibilityRepository = visibilityRepository;
    mIntermediateRasterExtentResolver = intermediateRasterExtentResolver;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
    mIntermediateRasterPresentation = intermediateRasterPresentation;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    SubscriptionRequest setRasterRequest =
        factory.create(Observable.just(mIntermediateRasterPresentation),
            irObservable -> irObservable.map(this::isToDisplay)
                .doOnNext(regardless -> hideCurrentlySelectedRaster())
                .filter(isToDisplay -> isToDisplay)
                .doOnNext(flag -> displayRaster())
                .doOnNext(flag -> goToExtent()));
    return Collections.singletonList(setRasterRequest);
  }

  private void hideCurrentlySelectedRaster() {
    String currentlyVisibleName = mVisibilityRepository.getCurrentlyVisibleName();
    if (currentlyVisibleName != null) {
      mVisibilityRepository.addChange(
          new IntermediateRasterVisibilityChange(false, currentlyVisibleName));
    }
  }

  private boolean isToDisplay(IntermediateRasterPresentation raster) {
    return !mVisibilityRepository.isVisible(raster.getName());
  }

  private void displayRaster() {
    String rasterName = mIntermediateRasterPresentation.getName();
    mVisibilityRepository.addChange(new IntermediateRasterVisibilityChange(true, rasterName));
  }

  private void goToExtent() {
    Geometry extent = mIntermediateRasterExtentResolver.getExtent(mIntermediateRasterPresentation);
    mGoToLocationMapInteractorFactory.create(extent).execute();
  }
}
