package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import java.util.Collections;
import io.reactivex.Observable;

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
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest =
        factory.create(Observable.just(mKmlEntityInfo),
            kmlEntityInfoObservable -> kmlEntityInfoObservable.map(this::nullifyOnReselection)
                .doOnNext(mCurrentKmlEntityInfoRepository::setCurrentDisplayedItem));

    return Collections.singletonList(dataSubscriptionRequest);
  }

  private KmlEntityInfo nullifyOnReselection(KmlEntityInfo kmlEntityInfo) {
    return (kmlEntityInfo == null || isReselection(kmlEntityInfo)) ? null : kmlEntityInfo;
  }

  private boolean isReselection(KmlEntityInfo kmlEntityInfo) {
    return kmlEntityInfo.equals(mCurrentKmlEntityInfoRepository.getCurrentDisplayedItem());
  }
}
