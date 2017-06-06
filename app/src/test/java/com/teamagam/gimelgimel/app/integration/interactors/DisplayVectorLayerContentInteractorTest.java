package com.teamagam.gimelgimel.app.integration.interactors;

import com.teamagam.gimelgimel.data.layers.VectorLayersDataRepository;
import com.teamagam.gimelgimel.data.layers.VectorLayersVisibilityDataRepository;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.layers.SetVectorLayerVisibilityInteractor;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.core.Is.is;

public class DisplayVectorLayerContentInteractorTest extends BaseTest {

  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
  private VectorLayersRepository mVectorLayersRepository;
  private VisibilityStatusTestDisplayer mDisplayer;
  private VectorLayersVisibilityDataRepository mVectorLayersVisibilityRepository;

  @Before
  public void setUp() throws Exception {
    mVectorLayersRepository = new VectorLayersDataRepository();
    mDisplayer = new VisibilityStatusTestDisplayer();
    mVectorLayersVisibilityRepository = new VectorLayersVisibilityDataRepository();
    mDisplayVectorLayersInteractor =
        new DisplayVectorLayersInteractor(this::createTestScheduler, this::createTestScheduler,
            mVectorLayersRepository, mVectorLayersVisibilityRepository,
            Mockito.mock(LayersLocalCache.class), mDisplayer);
  }

  @Test
  public void executeThenSetVisibleVL_VLShouldBeVisible() throws Exception {
    //Arrange
    VectorLayer vl = createVectorLayer(1);
    mVectorLayersRepository.put(vl);

    //Act
    mDisplayVectorLayersInteractor.execute();
    executeSetVectorLayerVisibilityInteractor(vl.getId(), true);

    //Assert
    Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
  }

  @Test
  public void executeThenSetInvisibleVL_VLShouldBeInvisible() throws Exception {
    //Arrange
    VectorLayer vl = createVectorLayer(1);
    mVectorLayersRepository.put(vl);

    //Act
    mDisplayVectorLayersInteractor.execute();
    executeSetVectorLayerVisibilityInteractor(vl.getId(), false);

    //Assert
    Assert.assertThat(mDisplayer.getVisibility("1"), is(false));
  }

  @Test
  public void setVisibleVLThenExecute_VLShouldBeVisible() throws Exception {
    //Arrange
    VectorLayer vl = createVectorLayer(1);
    mVectorLayersRepository.put(vl);

    //Act
    executeSetVectorLayerVisibilityInteractor(vl.getId(), true);
    mDisplayVectorLayersInteractor.execute();

    //Assert
    Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
  }

  @Test
  public void setVisibleThenSetInvisible_VLShouldBeInvisible() throws Exception {
    //Arrange
    VectorLayer vl = createVectorLayer(1);
    mVectorLayersRepository.put(vl);

    //Act
    mDisplayVectorLayersInteractor.execute();
    executeSetVectorLayerVisibilityInteractor(vl.getId(), true);
    executeSetVectorLayerVisibilityInteractor(vl.getId(), false);

    //Assert
    Assert.assertThat(mDisplayer.getVisibility("1"), is(false));
  }

  @Test
  public void setVisibleThenSetVisibleAgain_VLShouldBeVisible() throws Exception {
    //Arrange
    VectorLayer vl = createVectorLayer(1);
    mVectorLayersRepository.put(vl);

    //Act
    mDisplayVectorLayersInteractor.execute();
    executeSetVectorLayerVisibilityInteractor(vl.getId(), true);
    executeSetVectorLayerVisibilityInteractor(vl.getId(), true);

    //Assert
    Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
  }

  @Test
  public void setFirstVisibleThenExecuteThenSetSecondVisible_BothShouldBeVisible()
      throws Exception {
    //Arrange
    VectorLayer vl1 = createVectorLayer(1);
    VectorLayer vl2 = createVectorLayer(2);
    mVectorLayersRepository.put(vl1);
    mVectorLayersRepository.put(vl2);

    //Act
    executeSetVectorLayerVisibilityInteractor(vl1.getId(), true);
    mDisplayVectorLayersInteractor.execute();
    executeSetVectorLayerVisibilityInteractor(vl2.getId(), true);

    //Assert
    Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    Assert.assertThat(mDisplayer.getVisibility("2"), is(true));
  }

  private Scheduler createTestScheduler() {
    return Schedulers.trampoline();
  }

  private VectorLayer createVectorLayer(int num) {
    return new VectorLayer(String.valueOf(num), "name" + num, null, VectorLayer.Severity.REGULAR,
        VectorLayer.Category.FIRST, 1);
  }

  private void executeSetVectorLayerVisibilityInteractor(String id, boolean isVisible) {
    VectorLayerVisibilityChange change = new VectorLayerVisibilityChange(id, isVisible);
    new SetVectorLayerVisibilityInteractor(this::createTestScheduler,
        mVectorLayersVisibilityRepository, change).execute();
  }

  private static class VisibilityStatusTestDisplayer
      implements DisplayVectorLayersInteractor.Displayer {
    private Map<String, Boolean> mVisibilityStatus;

    public VisibilityStatusTestDisplayer() {
      mVisibilityStatus = new TreeMap<>();
    }

    public boolean getVisibility(String id) {
      if (mVisibilityStatus.containsKey(id)) {
        return mVisibilityStatus.get(id);
      } else {
        throw new RuntimeException(String.format("VectorLayer with id %s was never displayed", id));
      }
    }

    @Override
    public void display(VectorLayerPresentation vectorLayerPresentation) {
      mVisibilityStatus.put(vectorLayerPresentation.getId(), vectorLayerPresentation.isShown());
    }
  }
}
