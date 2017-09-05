package com.teamagam.gimelgimel.data.timeplay.dynamic_layers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.Collections;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayersTimespanCalculatorTest extends BaseTest {

  private static final Date ZERO_DATE = new Date(0);

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private DynamicLayersTimespanCalculator mCalculator;
  private DynamicLayerDao mDao;
  private DynamicLayersEntityMapper mMapper;

  private void insertDynamicLayerWithoutEntities() {
    DynamicLayer dynamicLayer = new DynamicLayer("id", "name", "", 5, Collections.EMPTY_LIST);
    mDao.insertDynamicLayer(mMapper.mapToEntity(dynamicLayer));
  }

  private void insertDynamicLayers(int... timestamps) {
    for (int timestamp : timestamps) {
      insertDynamicLayer(timestamp);
    }
  }

  private void insertDynamicLayer(long timestamp) {
    DynamicLayerEntity dl = DynamicLayersTestUtils.createTestEntity("id", "name", timestamp);
    mDao.insertDynamicLayer(dl);
  }

  @Before
  public void setUp() throws Exception {
    mDao = mDbRule.getDb().dynamicLayerDao();
    mMapper = DynamicLayersTestUtils.createDynamicLayersEntityMapper();
    mCalculator = new DynamicLayersTimespanCalculator(mDao);
  }

  @Test
  public void onEmptyDb_returnZeroDateMinimum() throws Exception {
    assertThat(mCalculator.getMinimumGeoItemDate(), is(ZERO_DATE));
  }

  @Test
  public void onEmptyDb_returnZeroDateMaximum() throws Exception {
    assertThat(mCalculator.getMaximumGeoItemDate(), is(ZERO_DATE));
  }

  @Test
  public void minIgnoresDynamicLayerWithNoEntities() throws Exception {
    //Arrange
    insertDynamicLayerWithoutEntities();

    //Act
    Date res = mCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(res, is(ZERO_DATE));
  }

  @Test
  public void maxIgnoresDynamicLayerWithNoEntities() throws Exception {
    //Arrange
    insertDynamicLayerWithoutEntities();

    //Act
    Date res = mCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(res, is(ZERO_DATE));
  }

  @Test
  public void getMin() throws Exception {
    //Arrange
    int actualMin = 5;
    insertDynamicLayers(actualMin, actualMin + 1);

    //Act
    Date res = mCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(res, is(new Date(actualMin)));
  }

  @Test
  public void getMax() throws Exception {
    //Arrange
    int actualMax = 10;
    insertDynamicLayers(actualMax - 1, actualMax);

    //Act
    Date res = mCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(res, is(new Date(actualMax)));
  }
}