package com.teamagam.gimelgimel.data.layers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AlertedVectorLayerEntityDataRepositoryTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private AlertedVectorLayerDataRepository mSubject;

  private VectorLayer getVectorLayer(int version) {
    VectorLayer vl = mock(VectorLayer.class);
    when(vl.getId()).thenReturn("id");
    when(vl.getVersion()).thenReturn(version);
    return vl;
  }

  private VectorLayer markAsSelected(int version) {
    VectorLayer vl = getVectorLayer(version);
    mSubject.markAsAlerted(vl);
    return vl;
  }

  @Before
  public void setUp() throws Exception {
    AlertedVectorLayerDao dao = mDbRule.getDb().alertedVectorLayerDao();
    mSubject = new AlertedVectorLayerDataRepository(dao);
  }

  @Test
  public void trueOnMarked() throws Exception {
    //Arrange
    VectorLayer vl = getVectorLayer(1);
    mSubject.markAsAlerted(vl);

    //Act
    boolean isAlerted = mSubject.isAlerted(vl);

    //Assert
    assertThat(isAlerted, is(true));
  }

  @Test
  public void falseOnNew() throws Exception {
    //Arrange
    VectorLayer vl = getVectorLayer(1);

    //Act
    boolean isAlerted = mSubject.isAlerted(vl);

    //Assert
    assertThat(isAlerted, is(false));
  }

  @Test
  public void falseOnNewerVersion() throws Exception {
    //Arrange
    markAsSelected(1);
    VectorLayer vl2 = getVectorLayer(2);

    //Act
    boolean isAlerted = mSubject.isAlerted(vl2);

    //Assert
    assertThat(isAlerted, is(false));
  }

  @Test
  public void trueOnMultipleVersion() throws Exception {
    //Arrange
    markAsSelected(1);
    VectorLayer vl = markAsSelected(2);

    //Act
    boolean isAlerted = mSubject.isAlerted(vl);

    //Assert
    assertThat(isAlerted, is(true));
  }

  @Test
  public void trueOnOlderAlertedVersion() throws Exception {
    //Arrange
    VectorLayer vl = markAsSelected(1);
    markAsSelected(2);

    //Act
    boolean isAlerted = mSubject.isAlerted(vl);

    //Assert
    assertThat(isAlerted, is(true));
  }
}