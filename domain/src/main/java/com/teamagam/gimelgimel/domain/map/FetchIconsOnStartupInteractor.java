package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.map.repository.IconsRepository;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import java.util.Collections;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;

@AutoFactory
public class FetchIconsOnStartupInteractor extends BaseDataInteractor {
  private static final Logger sLogger =
      LoggerFactory.create(FetchIconsOnStartupInteractor.class.getSimpleName());

  private RetryWithDelay mRetryStrategy;
  private IconsFetcher mIconsFetcher;
  private IconsRepository mRepository;
  private Action mOnCompleteAction;

  public FetchIconsOnStartupInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided IconsFetcher iconsFetcher,
      @Provided IconsRepository repository,
      @Provided RetryWithDelay retryStrategy,
      Action onCompleteAction) {
    super(threadExecutor);
    mIconsFetcher = iconsFetcher;
    mRepository = repository;
    mRetryStrategy = retryStrategy;
    mOnCompleteAction = onCompleteAction;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(factory.create(Observable.just(SIGNAL),
        observable -> observable.flatMapIterable(signal -> mIconsFetcher.fetchIcons())
            .retryWhen(mRetryStrategy)
            .doOnNext(mRepository::put)
            .doOnError(
                tr -> sLogger.w("An error occurred while trying to fetch icons from server: ", tr))
            .doOnComplete(mOnCompleteAction)));
  }
}
