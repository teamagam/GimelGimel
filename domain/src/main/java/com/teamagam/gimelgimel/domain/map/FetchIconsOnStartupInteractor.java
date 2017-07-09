package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.map.repository.IconsRepository;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.Collections;
import javax.inject.Inject;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

public class FetchIconsOnStartupInteractor extends BaseDataInteractor {
  private static final Logger sLogger =
      LoggerFactory.create(FetchIconsOnStartupInteractor.class.getSimpleName());

  private final Function<Observable<? extends Throwable>, Observable<?>> mRetryStrategy;
  private IconsFetcher mIconsFetcher;
  private IconsRepository mRepository;

  @Inject
  public FetchIconsOnStartupInteractor(ThreadExecutor threadExecutor,
      IconsFetcher iconsFetcher,
      IconsRepository repository,
      Function<Observable<? extends Throwable>, Observable<?>> retryStrategy) {
    super(threadExecutor);
    mIconsFetcher = iconsFetcher;
    mRepository = repository;
    mRetryStrategy = retryStrategy;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(factory.create(Observable.just(SIGNAL),
        observable -> observable.flatMapIterable(signal -> mIconsFetcher.fetchIcons())
            .doOnNext(mRepository::put)
            .retryWhen(mRetryStrategy)
            .doOnError(
                tr -> sLogger.w("An error occurred while trying to fetch icons from server: ",
                    tr))));
  }
}
