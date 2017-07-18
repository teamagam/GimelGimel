package com.teamagam.gimelgimel.domain.icons;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisplayIconsInteractorTest extends BaseTest {

  private DisplayIconsInteractor.Displayer mDisplayer;
  private DisplayIconsInteractor mInteractor;
  private IconsRepository mRepository;

  @Before
  public void setUp() throws Exception {
    mRepository = mock(IconsRepository.class);
    mDisplayer = spy(DisplayIconsInteractor.Displayer.class);
    mInteractor =
        new DisplayIconsInteractor(Schedulers::trampoline, Schedulers::trampoline, mRepository,
            mDisplayer);
  }

  @Test
  public void testDisplayerCall_ShouldCalledOnce() {
    // Arrange
    Icon icon = mock(Icon.class);
    ArrayList<Icon> icons = new ArrayList<>();
    icons.add(icon);
    when(mRepository.getAvailableIcons()).thenReturn(icons);

    // Act
    mInteractor.execute();

    // Assert
    verify(mDisplayer).display(icon);
  }
}