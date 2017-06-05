package com.teamagam.gimelgimel.domain.rasters;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRastersRepository;
import java.util.Collections;
import javax.inject.Inject;
import io.reactivex.Observable;

public class LoadIntermediateRastersInteractor extends BaseDataInteractor {

  private static final boolean DEFAULT_INTERMEDIATE_RASTER_VISIBILITY = false;

  private final IntermediateRastersLocalStorage mRastersLocalStorage;
  private final IntermediateRastersRepository mRastersRepository;
  private final IntermediateRasterVisibilityRepository mVisibilityRepository;

  @Inject
  public LoadIntermediateRastersInteractor(ThreadExecutor threadExecutor,
      IntermediateRastersLocalStorage rasterLocalStorage,
      IntermediateRastersRepository rastersRepository,
      IntermediateRasterVisibilityRepository visibilityRepository) {
    super(threadExecutor);
    mRastersLocalStorage = rasterLocalStorage;
    mRastersRepository = rastersRepository;
    mVisibilityRepository = visibilityRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest subscriptionRequest = factory.create(Observable.just(null),
        observable -> observable.flatMapIterable(x -> mRastersLocalStorage.getExistingRasters())
            .doOnNext(this::addToRepoAndSetVisibility));

    return Collections.singleton(subscriptionRequest);
  }

  private void addToRepoAndSetVisibility(IntermediateRaster raster) {
    mRastersRepository.add(raster);
    mVisibilityRepository.addChange(
        new IntermediateRasterVisibilityChange(DEFAULT_INTERMEDIATE_RASTER_VISIBILITY,
            raster.getName()));
  }
}
