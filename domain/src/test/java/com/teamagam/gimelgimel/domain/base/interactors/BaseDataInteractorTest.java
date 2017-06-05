package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import org.junit.Before;
import org.junit.Test;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.plugins.RxJavaSchedulersHook;
import io.reactivex.subjects.Subject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class BaseDataInteractorTest extends BaseTest {

  private Scheduler mNewThreadScheduler;
  private Thread mMainThread;
  private ThreadExecutor mThreadExecutor;
  private CountDownLatch mCountDownLatch;

  @Before
  public void setUp() throws Exception {
    mNewThreadScheduler = RxJavaSchedulersHook.createNewThreadScheduler();
    mMainThread = Thread.currentThread();
    mThreadExecutor = () -> mNewThreadScheduler;
    mCountDownLatch = new CountDownLatch(1);
  }

  @Test
  public void observeOnDataThread_subjectAsSource() throws Exception {
    Subject<Object> subject = new SerializedSubjectBuilder().build();
    BaseDataInteractor interactor = buildTestInteractor(subject);

    interactor.execute();
    subject.onNext(null);

    mCountDownLatch.await();
  }

  @Test
  public void observeOnDataThread_coldObservableAsSource() throws Exception {
    BaseDataInteractor interactor = buildTestInteractor(Observable.just(null));

    interactor.execute();

    mCountDownLatch.await();
  }

  private BaseDataInteractor buildTestInteractor(final Observable<Object> observable) {
    return new BaseDataInteractor(mThreadExecutor) {
      @Override
      protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return Collections.singletonList(
            factory.create(observable, objectObservable -> objectObservable.doOnNext(x -> {
                  assertNotOnMainThread();
                  countDown();
                })

            ));
      }
    };
  }

  private void assertNotOnMainThread() {
    assertThat(Thread.currentThread(), is(not(mMainThread)));
  }

  private void countDown() {
    mCountDownLatch.countDown();
  }
}