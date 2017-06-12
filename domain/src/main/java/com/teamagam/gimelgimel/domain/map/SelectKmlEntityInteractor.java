package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import io.reactivex.Observable;
import java.util.Collections;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

@AutoFactory
public class SelectKmlEntityInteractor extends BaseDataInteractor {

  private final SingleDisplayedItemRepository<KmlEntityInfo> mCurrentKmlEntityInfoRepository;
  private final KmlEntityInfo mKmlEntityInfo;

  public SelectKmlEntityInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided SingleDisplayedItemRepository<KmlEntityInfo> currentKmlEntityInfoRepository,
      KmlEntityInfo kmlEntityInfo) {
    super(threadExecutor);
    mCurrentKmlEntityInfoRepository = currentKmlEntityInfoRepository;
    mKmlEntityInfo = kmlEntityInfo;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest = factory.create(Observable.just(SIGNAL),
        signalObservable -> signalObservable.doOnNext(signal -> updateCurrentKmlEntityInfo()));

    return Collections.singletonList(dataSubscriptionRequest);
  }

  private void updateCurrentKmlEntityInfo() {
    if (mKmlEntityInfo == null || isReselection(mKmlEntityInfo)) {
      mCurrentKmlEntityInfoRepository.clear();
    } else {
      mCurrentKmlEntityInfoRepository.setItem(mKmlEntityInfo);
    }
  }

  private boolean isReselection(KmlEntityInfo kmlEntityInfo) {
    return kmlEntityInfo.equals(mCurrentKmlEntityInfoRepository.getItem());
  }
}
