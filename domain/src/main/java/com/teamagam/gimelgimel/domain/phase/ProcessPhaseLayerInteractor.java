package com.teamagam.gimelgimel.domain.phase;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.phase.visibility.PhaseLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.phase.visibility.PhaseLayerVisibilityRepository;
import io.reactivex.Observable;
import javax.inject.Inject;

public class ProcessPhaseLayerInteractor extends BaseSingleDataInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(ProcessPhaseLayerInteractor.class.getSimpleName());

  private final PhaseLayerRepository mPhaseLayerRepository;
  private final PhaseLayerVisibilityRepository mPhaseLayerVisibilityRepository;

  @Inject
  protected ProcessPhaseLayerInteractor(ThreadExecutor threadExecutor,
      PhaseLayerRepository phaseLayerRepository,
      PhaseLayerVisibilityRepository phaseLayerVisibilityRepository) {
    super(threadExecutor);
    mPhaseLayerRepository = phaseLayerRepository;
    mPhaseLayerVisibilityRepository = phaseLayerVisibilityRepository;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mPhaseLayerRepository),
        obsv -> obsv.flatMap(PhaseLayerRepository::getObservable)
            .doOnNext(this::log)
            .doOnNext(this::setVisibility));
  }

  private void log(PhaseLayer phaseLayer) {
    sLogger.v("Processing phase layer " + phaseLayer.toString());
  }

  private void setVisibility(PhaseLayer phaseLayer) {
    mPhaseLayerVisibilityRepository.addChange(
        new PhaseLayerVisibilityChange(true, phaseLayer.getId()));
  }
}