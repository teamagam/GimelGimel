package com.teamagam.gimelgimel.data.dynamicLayers.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils.assertEqualToStrings;
import static com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils.createTestLayerEntity;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayerDaoTest extends BaseTest {

  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String UPDATED_NAME = "updated_name";
  public static final String ID_2 = "id_2";
  public static final String NAME_2 = "name_2";
  public static final int TIMESTAMP = 0;
  public static final int UPDATED_TIMESTAMP = 1;

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private DynamicLayerDao mDao;
  private DynamicLayerEntity mEntity;
  private DynamicLayerEntity mEntityUpdated;
  private DynamicLayerEntity mEntity2;

  @Before
  public void setUp() {
    mDao = mDbRule.getDb().dynamicLayerDao();

    mEntity = createTestLayerEntity(ID, NAME, TIMESTAMP);
    mEntityUpdated = createTestLayerEntity(ID, UPDATED_NAME, UPDATED_TIMESTAMP);
    mEntity2 = createTestLayerEntity(ID_2, NAME_2);
  }

  @Test
  public void canRetrieveById() {
    // Act
    mDao.insertDynamicLayer(mEntity);

    // Assert
    assertEqualToStrings(mEntity, mDao.getLatestDynamicLayerById(ID));
  }

  @Test
  public void getAllDynamicLayersRetrievesAllLayers() {
    // Arrange
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntity2);

    // Act
    Object[] actuals = mDao.getLatestDynamicLayers().toArray();

    // Assert
    DynamicLayerEntity[] expecteds = { mEntity, mEntity2 };
    assertEqualToStrings(expecteds, actuals);
  }

  @Test
  public void returnsLatestByTimestamp() {
    // Act
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntityUpdated);

    // Assert
    assertEqualToStrings(mEntityUpdated, mDao.getLatestDynamicLayerById(ID));
  }

  @Test
  public void getLatestDynamicLayers() throws Exception {
    //Arrange
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntityUpdated);
    mDao.insertDynamicLayer(mEntity2);

    //Act
    List<DynamicLayerEntity> allDynamicLayers = mDao.getLatestDynamicLayers();

    //Assert
    DynamicLayerEntity[] expecteds = { mEntityUpdated, mEntity2 };
    assertEqualToStrings(expecteds, allDynamicLayers.toArray());
  }
}
