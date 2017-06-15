package com.teamagam.gimelgimel.domain.rasters;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRasterVisibilityChange;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import io.reactivex.Observable;
import java.util.Collections;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

@AutoFactory
public class SetIntermediateRasterInteractor extends BaseDataInteractor {

  private final IntermediateRasterVisibilityRepository mVisibilityRepository;
  private final String mIntermediateRasterName;

  public SetIntermediateRasterInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided IntermediateRasterVisibilityRepository visibilityRepository,
      String intermediateRasterName) {
    super(threadExecutor);
    mVisibilityRepository = visibilityRepository;
    mIntermediateRasterName = intermediateRasterName;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    SubscriptionRequest setRasterRequest = factory.create(Observable.just(SIGNAL),
        signalObservable -> signalObservable.doOnNext(signal -> removeOldIrAndSetNew()));
    return Collections.singletonList(setRasterRequest);
  }

  private void removeOldIrAndSetNew() {
    removeOld();
    setNew(mIntermediateRasterName);
  }

  private void removeOld() {
    String currentlyVisibleName = mVisibilityRepository.getCurrentlyVisibleName();
    if (currentlyVisibleName != null) {
      mVisibilityRepository.addChange(
          new IntermediateRasterVisibilityChange(false, currentlyVisibleName));
    }
  }

  private void setNew(String rasterName) {
    if (rasterName != null) {
      mVisibilityRepository.addChange(new IntermediateRasterVisibilityChange(true, rasterName));
    }
  }
}
