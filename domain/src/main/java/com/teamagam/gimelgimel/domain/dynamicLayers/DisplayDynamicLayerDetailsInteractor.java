package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import com.teamagam.gimelgimel.domain.phase.PhaseLayerRepository;
import io.reactivex.Observable;

@AutoFactory
public class DisplayDynamicLayerDetailsInteractor extends BaseSingleDisplayInteractor {

  private final DynamicLayersRepository mDynamicLayersRepository;
  private final PhaseLayerRepository mPhaseLayerRepository;
  private final String mDynamicLayerId;
  private final Displayer mDisplayer;

  public DisplayDynamicLayerDetailsInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided DynamicLayersRepository dynamicLayersRepository,
      @Provided PhaseLayerRepository phaseLayerRepository,
      Displayer displayer,
      String dynamicLayerId) {
    super(threadExecutor, postExecutionThread);
    mDynamicLayersRepository = dynamicLayersRepository;
    mPhaseLayerRepository = phaseLayerRepository;
    mDisplayer = displayer;
    mDynamicLayerId = dynamicLayerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mDynamicLayersRepository),
        repoObsrv -> repoObsrv.flatMap(DynamicLayersRepository::getObservable)
            .mergeWith(getFlattenedPhasesObservable())
            .filter(dl -> dl.getId().equals(mDynamicLayerId)), mDisplayer::display);
  }

  private Observable<DynamicLayer> getFlattenedPhasesObservable() {
    return mPhaseLayerRepository.getObservable().flatMapIterable(PhaseLayer::getPhases);
  }

  public interface Displayer {
    void display(DynamicLayer dynamicLayer);
  }
}
