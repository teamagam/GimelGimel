package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.icons.ServerIcon;
import com.teamagam.gimelgimel.domain.map.repository.IconsRepository;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FetchIconsOnStartupInteractorTest extends BaseTest {

  private ServerIcon mServerIcon1;
  private ServerIcon mServerIcon2;
  private IconsFetcher mIconsFetcher;
  private IconsRepository mRepository;

  @Before
  public void setUp() throws Exception {
    mServerIcon1 = mock(ServerIcon.class);
    mServerIcon2 = mock(ServerIcon.class);
    mIconsFetcher = mock(IconsFetcher.class);
    mRepository = spy(IconsRepository.class);
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
    sleep();
    verify(mIconsFetcher, times(3)).fetchIcons();
  }

  private void executeInteractor() {
    FetchIconsOnStartupInteractor interactor =
        new FetchIconsOnStartupInteractor(Schedulers::trampoline, mIconsFetcher, mRepository,
            new RetryWithDelay(3, 0));
    interactor.execute();
  }

  private void sleep() {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}