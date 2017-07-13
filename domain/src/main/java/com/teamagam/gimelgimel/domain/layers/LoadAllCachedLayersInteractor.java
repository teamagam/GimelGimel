package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import io.reactivex.Observable;
import java.util.Collections;
import javax.inject.Inject;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

public class LoadAllCachedLayersInteractor extends BaseDataInteractor {

  private final LayersLocalCache mLayersLocalCache;
  private final VectorLayersRepository mVectorLayersRepository;

  @Inject
  public LoadAllCachedLayersInteractor(ThreadExecutor threadExecutor,
      LayersLocalCache layersLocalCache,
      VectorLayersRepository vectorLayersRepository) {
    super(threadExecutor);
    mLayersLocalCache = layersLocalCache;
    mVectorLayersRepository = vectorLayersRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest<?> request = factory.create(Observable.just(SIGNAL),
        observable -> observable.flatMapIterable(x -> mLayersLocalCache.getAllCachedLayers())
            .map(this::recreateAsUnimportant)
            .doOnNext(mVectorLayersRepository::put));
    return Collections.singletonList(request);
  }

  private VectorLayer recreateAsUnimportant(VectorLayer vectorLayer) {
    return VectorLayer.copyWithDifferentSeverity(vectorLayer, VectorLayer.Severity.REGULAR);
  }
}
