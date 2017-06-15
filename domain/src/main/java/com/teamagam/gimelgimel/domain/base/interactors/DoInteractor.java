package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.subscribers.DummyObserver;
import io.reactivex.Observable;
import io.reactivex.observers.ResourceObserver;

public abstract class DoInteractor<T> extends AbsInteractor<T> {

  private final ThreadExecutor threadExecutor;

  protected DoInteractor(ThreadExecutor threadExecutor) {
    this.threadExecutor = threadExecutor;
  }

  protected Observable<T> buildObservable() {
    return buildUseCaseObservable().subscribeOn(threadExecutor.getScheduler());
  }

  protected abstract Observable<T> buildUseCaseObservable();

  protected ResourceObserver<T> getObserver() {
    return new DummyObserver<>();
  }
}
