package com.teamagam.gimelgimel.domain.phase;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.phase.repository.PhaseLayerRepository;
import io.reactivex.Observable;
import javax.inject.Inject;

public class ProcessPhaseLayerInteractor extends BaseSingleDataInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(ProcessPhaseLayerInteractor.class.getSimpleName());

  private final PhaseLayerRepository mPhaseLayerRepository;

  @Inject
  protected ProcessPhaseLayerInteractor(ThreadExecutor threadExecutor,
      PhaseLayerRepository phaseLayerRepository) {
    super(threadExecutor);
    mPhaseLayerRepository = phaseLayerRepository;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mPhaseLayerRepository),
        obsv -> obsv.flatMap(PhaseLayerRepository::getObservable).doOnNext(this::log));
  }

  private void log(PhaseLayer phaseLayer) {
    sLogger.v("Processing phase layer " + phaseLayer.toString());
  }
}
