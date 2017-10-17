package com.teamagam.gimelgimel.domain.phase;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import io.reactivex.Observable;

@AutoFactory
public class DisplayPhaseLayerInteractor extends BaseSingleDisplayInteractor {

  private final PhaseLayerRepository mPhaseLayerRepository;
  private final Displayer mDisplayer;
  private final String mPhaseLayerId;

  public DisplayPhaseLayerInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided PhaseLayerRepository phaseLayerRepository,
      Displayer displayer,
      String phaseLayerId) {
    super(threadExecutor, postExecutionThread);
    mPhaseLayerRepository = phaseLayerRepository;
    mDisplayer = displayer;
    mPhaseLayerId = phaseLayerId;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mPhaseLayerId),
        idObsv -> idObsv.map(mPhaseLayerRepository::getById), mDisplayer::display);
  }

  public interface Displayer {
    void display(PhaseLayer layer);
  }
}
