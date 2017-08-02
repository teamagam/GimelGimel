package com.teamagam.gimelgimel.domain.rasters;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRastersRepository;

@AutoFactory
public class DisplayIntermediateRastersInteractor extends BaseSingleDisplayInteractor {

  private final IntermediateRastersRepository mIntermediateRastersRepository;
  private final IntermediateRasterVisibilityRepository mIntermediateRasterVisibilityRepository;
  private final Displayer mDisplayer;

  public DisplayIntermediateRastersInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided IntermediateRastersRepository intermediateRastersRepository,
      @Provided IntermediateRasterVisibilityRepository intermediateRasterVisibilityRepository,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mIntermediateRastersRepository = intermediateRastersRepository;
    mIntermediateRasterVisibilityRepository = intermediateRasterVisibilityRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

    return factory.create(mIntermediateRasterVisibilityRepository.getChangesObservable(),
        changeObservable -> changeObservable.map(this::getPresentation), mDisplayer::display);
  }

  private IntermediateRasterPresentation getPresentation(IntermediateRasterVisibilityChange change) {
    IntermediateRaster ir = mIntermediateRastersRepository.get(change.getId());
    if (change.isVisible()) {
      return new IntermediateRasterPresentation(ir.getName(), ir.getLocalUri(), true);
    } else {
      return new IntermediateRasterPresentation(ir.getName(), ir.getLocalUri(), false);
    }
  }

  public interface Displayer {
    void display(IntermediateRasterPresentation intermediateRasterPresentation);
  }
}
