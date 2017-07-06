package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.IconsRepository;
import io.reactivex.Observable;
import java.util.Collections;

public class FetchIconsOnStartupInteractor extends BaseDataInteractor {
  private IconsFetcher mIconsFetcher;
  private IconsRepository mRepository;

  public FetchIconsOnStartupInteractor(ThreadExecutor threadExecutor,
      IconsFetcher iconsFetcher,
      IconsRepository repository) {
    super(threadExecutor);
    mIconsFetcher = iconsFetcher;
    mRepository = repository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(
        factory.create(Observable.fromIterable(mIconsFetcher.fetchIcons()),
            o -> o.doOnNext(mRepository::put)));
  }
}
