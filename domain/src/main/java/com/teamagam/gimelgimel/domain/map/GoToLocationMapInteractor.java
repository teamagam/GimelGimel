package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import io.reactivex.Observable;
import java.util.Collections;

@AutoFactory(allowSubclasses = true)
public class GoToLocationMapInteractor extends BaseDataInteractor {

  private ViewerCameraController mViewerCameraController;
  private Geometry mGeometry;

  protected GoToLocationMapInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided ViewerCameraController viewerCameraController,
      Geometry geometry) {
    super(threadExecutor);
    mViewerCameraController = viewerCameraController;
    mGeometry = geometry;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(factory.create(Observable.just(mGeometry),
        observable -> observable.doOnNext(mViewerCameraController::setViewerCamera)));
  }
}
