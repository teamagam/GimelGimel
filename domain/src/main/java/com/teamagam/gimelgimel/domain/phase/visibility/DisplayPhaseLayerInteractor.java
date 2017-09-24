package com.teamagam.gimelgimel.domain.phase.visibility;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import com.teamagam.gimelgimel.domain.phase.PhaseLayerRepository;
import io.reactivex.Observable;

@AutoFactory
public class DisplayPhaseLayerInteractor extends BaseSingleDisplayInteractor {

  private final PhaseLayerVisibilityRepository mVisibilityRepository;
  private final PhaseLayerRepository mPhaseLayerRepository;
  private final Displayer mDisplayer;

  public DisplayPhaseLayerInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided PhaseLayerVisibilityRepository visibilityRepository,
      @Provided PhaseLayerRepository phaseLayerRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mVisibilityRepository = visibilityRepository;
    mPhaseLayerRepository = phaseLayerRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mVisibilityRepository),
        obsv -> obsv.flatMap(PhaseLayerVisibilityRepository::getChangesObservable)
            .map(this::toPresentation), mDisplayer::display);
  }

  private PhaseLayerPresentation toPresentation(PhaseLayerVisibilityChange change) {
    PhaseLayer layer = mPhaseLayerRepository.getById(change.getId());
    return new PhaseLayerPresentation(layer, change.isVisible());
  }

  public interface Displayer {
    void display(PhaseLayerPresentation plp);
  }
}
