package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.icons.FetchIconsOnStartupInteractor;
import com.teamagam.gimelgimel.domain.icons.IconsFetcher;
import com.teamagam.gimelgimel.domain.icons.entities.ServerIcon;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.TestScheduler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchIconsOnStartupInteractorTest extends BaseTest {

  public static final int MAX_RETRIES = 3;
  public static final int NO_DELAY = 0;

  private ServerIcon mServerIcon1;
  private ServerIcon mServerIcon2;
  private IconsFetcher mIconsFetcher;
  private IconsRepository mRepository;
  private Action mAction;
  private TestScheduler mTestScheduler;

  @Before
  public void setUp() throws Exception {
    mServerIcon1 = mock(ServerIcon.class);
    mServerIcon2 = mock(ServerIcon.class);
    mIconsFetcher = mock(IconsFetcher.class);
    mRepository = spy(IconsRepository.class);
    mAction = spy(Action.class);

    mTestScheduler = new TestScheduler();
  }

  @Test
  public void whenIconEmitted_thenItIsPutInRepo() throws IOException {
    // Arrange
    when(mIconsFetcher.fetchIcons()).thenReturn(Collections.singletonList(mServerIcon1));

    // Act
    executeInteractor();

    // Assert
    verify(mRepository).put(mServerIcon1);
  }

  @Test
  public void whenManyIconsEmitted_thenTheyArePutInRepo() throws IOException {
    // Arrange
    when(mIconsFetcher.fetchIcons()).thenReturn(Arrays.asList(mServerIcon1, mServerIcon2));

    // Act
    executeInteractor();

    // Assert
    verify(mRepository).put(mServerIcon1);
    verify(mRepository).put(mServerIcon2);
  }

  @Test
  public void whenErrorOccurs_thenRetryFetching() throws IOException {
    // Arrange
    when(mIconsFetcher.fetchIcons()).thenThrow(IOException.class);

    // Act
    executeInteractor();

    // Assert
    verify(mIconsFetcher, times(MAX_RETRIES)).fetchIcons();
  }

  @Test
  public void whenFetchingComplete_thenActAsDefined() throws Exception {
    // Arrange
    when(mIconsFetcher.fetchIcons()).thenReturn(Collections.singletonList(mServerIcon1));

    // Act
    executeInteractor();

    // Assert
    verify(mAction).run();
  }

  private void executeInteractor() {
    ThreadExecutor threadExecutor = () -> mTestScheduler;
    RetryWithDelay retryStrategy = new RetryWithDelay(MAX_RETRIES, NO_DELAY, threadExecutor);

    new FetchIconsOnStartupInteractor(threadExecutor, mIconsFetcher, mRepository, retryStrategy,
        mAction).execute();

    mTestScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);
  }
}