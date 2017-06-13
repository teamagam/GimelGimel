package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.util.Collections;
import javax.inject.Inject;
import rx.Observable;

public class LoadAllCachedLayersInteractor extends BaseDataInteractor {

  private final LayersLocalCache mLayersLocalCache;
  private final ProcessNewVectorLayerInteractorFactory mProcessNewVectorLayerInteractorFactory;

  @Inject
  public LoadAllCachedLayersInteractor(ThreadExecutor threadExecutor,
      LayersLocalCache layersLocalCache,
      ProcessNewVectorLayerInteractorFactory processNewVectorLayerInteractorFactory) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mProcessNewVectorLayerInteractorFactory = processNewVectorLayerInteractorFactory;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest<?> request = factory.create(Observable.just(null),
        observable -> observable.flatMapIterable(x -> mLayersLocalCache.getAllCachedLayers())
            .map(this::recreateAsUnimportant)
            .doOnNext(vl -> mProcessNewVectorLayerInteractorFactory.create(vl).execute()));
    return Collections.singletonList(request);
  }

  private VectorLayer recreateAsUnimportant(VectorLayer vectorLayer) {
    VectorLayer newVectorLayer =
        VectorLayer.copyWithDifferentSeverity(vectorLayer, VectorLayer.Severity.REGULAR);
    newVectorLayer.setURl(null);

    return vectorLayer;
  }
}
