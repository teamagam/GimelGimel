package com.teamagam.gimelgimel.domain.dynamicLayers;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayerVisibilityRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisplayDynamicLayersInteractorTest extends BaseTest {

  private DynamicLayer mDynamicLayer;
  private DisplayDynamicLayersInteractor.Displayer mDisplayer;
  private DynamicLayersRepository mRepository;
  private DynamicLayerVisibilityRepository mDynamicLayerVisibilityRepository;

  @Before
  public void setUp() throws Exception {
    mDynamicLayer = new DynamicLayer("id", "name", Collections.EMPTY_LIST);
    mDisplayer = spy(DisplayDynamicLayersInteractor.Displayer.class);
    mRepository = mock(DynamicLayersRepository.class);
    mDynamicLayerVisibilityRepository = mock(DynamicLayerVisibilityRepository.class);
  }

  @Test
  public void whenChangeIsShown_displayWithShownPresentation() throws Exception {
    // Arrange
    setupDynamicLayerState(true);

    // Act
    executeInteractor();

    // Assert
    assertPresentationDisplayed(true);
  }

  @Test
  public void whenChangeIsHidden_displayWithHiddenPresentation() throws Exception {
    //Arrange
    setupDynamicLayerState(false);

    //Act
    executeInteractor();

    //Assert
    assertPresentationDisplayed(false);
  }

  private void setupDynamicLayerState(boolean isVisible) {
    DynamicLayerVisibilityChange dynamicLayerVisibilityChange =
        new DynamicLayerVisibilityChange(isVisible, mDynamicLayer.getId());
    when(mDynamicLayerVisibilityRepository.getChangesObservable()).thenReturn(
        Observable.just(dynamicLayerVisibilityChange));
    when(mRepository.getById(mDynamicLayer.getId())).thenReturn(mDynamicLayer);
  }

  private void executeInteractor() {
    new DisplayDynamicLayersInteractor(Schedulers::trampoline, Schedulers::trampoline, mRepository,
        mDynamicLayerVisibilityRepository, mDisplayer).execute();
  }

  private void assertPresentationDisplayed(boolean visiblity) {
    ArgumentCaptor<DynamicLayerPresentation> tArgumentCaptor =
        ArgumentCaptor.forClass(DynamicLayerPresentation.class);
    verify(mDisplayer).display(tArgumentCaptor.capture());

    DynamicLayerPresentation value = tArgumentCaptor.getValue();
    assertThat(value.isShown(), is(visiblity));
    assertThat(value.getId(), is(mDynamicLayer.getId()));
  }
}