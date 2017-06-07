package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.RepeatedBackoffTaskRunner;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observable;

public class StartFetchingMessagesInteractor extends DoInteractor {

  private final RepeatedBackoffTaskRunner mMessagesTaskRunner;

  @Inject
  public StartFetchingMessagesInteractor(ThreadExecutor threadExecutor,
      @Named("message poller")
          RepeatedBackoffTaskRunner taskRunner) {
    super(threadExecutor);
    mMessagesTaskRunner = taskRunner;
  }

  @Override
  protected Observable buildUseCaseObservable() {
    return Observable.just(mMessagesTaskRunner).doOnNext(RepeatedBackoffTaskRunner::start);
  }
}
