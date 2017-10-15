package com.teamagam.gimelgimel.domain.phase.visibility;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import com.teamagam.gimelgimel.domain.phase.PhaseLayerRepository;
import io.reactivex.Observable;

@AutoFactory
public class OnPhaseLayerListingClickInteractor extends BaseSingleDataInteractor {

  private final PhaseLayerVisibilityRepository mPhaseLayerVisibilityRepository;
  private final PhaseLayerRepository mPhaseLayerRepository;
  private final String mPhaseLayerId;

  protected OnPhaseLayerListingClickInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PhaseLayerVisibilityRepository phaseLayerVisibilityRepository,
      @Provided PhaseLayerRepository phaseLayerRepository,
      String phaseLayerId) {
    super(threadExecutor);
    mPhaseLayerVisibilityRepository = phaseLayerVisibilityRepository;
    mPhaseLayerRepository = phaseLayerRepository;
    mPhaseLayerId = phaseLayerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mPhaseLayerId),
        idOsb -> idOsb.map(mPhaseLayerRepository::getById)
            .map(this::getToggledVisibilityChange)
            .doOnNext(mPhaseLayerVisibilityRepository::addChange));
  }

  private PhaseLayerVisibilityChange getToggledVisibilityChange(PhaseLayer phaseLayer) {
    String phaseLayerId = phaseLayer.getId();
    boolean toggledVisibility = !mPhaseLayerVisibilityRepository.isVisible(phaseLayerId);
    return new PhaseLayerVisibilityChange(toggledVisibility, phaseLayerId);
  }
}