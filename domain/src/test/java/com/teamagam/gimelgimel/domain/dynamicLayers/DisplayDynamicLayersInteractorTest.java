package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisplayDynamicLayersInteractorTest extends BaseTest {

  private DynamicLayer mDynamicLayer;
  private DisplayDynamicLayersInteractor.Displayer mDisplayer;
  private DynamicLayersRepository mRepository;

  @Before
  public void setUp() throws Exception {
    mDynamicLayer = new DynamicLayer("id", "name", Collections.EMPTY_LIST);
    mDisplayer = spy(DisplayDynamicLayersInteractor.Displayer.class);
    mRepository = mock(DynamicLayersRepository.class);
  }

  @Test
  public void displaysEmittedLayers() throws Exception {
    // Arrange
    when(mRepository.getObservable()).thenReturn(Observable.just(mDynamicLayer));

    // Act
    executeInteractor();

    // Assert
    verify(mDisplayer).display(mDynamicLayer);
  }

  private void executeInteractor() {
    new DisplayDynamicLayersInteractor(Schedulers::trampoline, Schedulers::trampoline, mRepository,
        mDisplayer).execute();
  }
}