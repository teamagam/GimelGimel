package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import io.reactivex.Observable;

@AutoFactory(allowSubclasses = true)
public class GoToLocationMapInteractor extends DoInteractor {

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
  protected Observable buildUseCaseObservable() {
    return Observable.just(mGeometry).doOnNext(mViewerCameraController::setViewerCamera);
  }
}
