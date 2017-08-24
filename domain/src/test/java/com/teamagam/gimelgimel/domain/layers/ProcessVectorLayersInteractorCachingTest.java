package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.AlertedVectorLayerRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.AlertMessageTextCreator;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessVectorLayersInteractorCachingTest extends BaseTest {

  public static final int MAX_RETRIES = 3;
  public static final int NO_DELAY = 0;

  private VectorLayersRepository mVectorLayersRepository;
  private VectorLayer mVectorLayer1;
  private VectorLayer mVectorLayer2;
  private LayersLocalCache mCache;

  @Before
  public void setUp() throws Exception {
    mVectorLayer1 = mock(VectorLayer.class);
    when(mVectorLayer1.getUrl()).thenReturn(new URL("http://www.fake.com"));
    mVectorLayer2 = mock(VectorLayer.class);
    when(mVectorLayer2.getUrl()).thenReturn(new URL("http://www.fake2.com"));

    mVectorLayersRepository = mock(VectorLayersRepository.class);
    when(mVectorLayersRepository.getVectorLayersObservable()).thenReturn(
        Observable.just(mVectorLayer1, mVectorLayer2));

    mCache = Mockito.spy(LayersLocalCache.class);
    when(mCache.isCached(mVectorLayer1)).thenReturn(false);
    when(mCache.isCached(mVectorLayer2)).thenReturn(false);
  }

  @Test
  public void whenNonCachedVectorLayerEmitted_cacheIt() throws Exception {
    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mCache).cache(mVectorLayer1);
  }

  @Test
  public void whenCannotCacheVectorLayer_InteractorIgnoresItAndContinues() throws Exception {
    // Arrange
    when(mCache.cache(mVectorLayer1)).thenThrow(new RuntimeException("Cannot cache mVectorLayer1"));

    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mCache).cache(mVectorLayer2);
  }

  private void executeInteractor() {
    new ProcessVectorLayersInteractor(Schedulers::trampoline, mCache, mVectorLayersRepository,
        mock(VectorLayersVisibilityRepository.class), mock(AlertedVectorLayerRepository.class),
        mock(MessagesRepository.class),
        new RetryWithDelay(MAX_RETRIES, NO_DELAY, Schedulers::trampoline),
        mock(AlertMessageTextCreator.class)).execute();
  }
}