package com.teamagam.gimelgimel.data.dynamicLayers.room.dao;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayerDaoTest extends BaseTest {
  /*

  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String UPDATED_NAME = "updated_name";
  public static final String ID_2 = "id_2";
  public static final String NAME_2 = "name_2";

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private AppDatabase mDb;
  private DynamicLayerDao mDao;
  private DynamicLayerEntity mEntity;
  private DynamicLayerEntity mEntityUpdated;
  private DynamicLayerEntity mEntity2;

  @Before
  public void setUp() {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = DynamicLayersTestUtils.getDB(context);
    mDao = mDb.dynamicLayerDao();

    mEntity = createTestEntity(ID, NAME);
    mEntityUpdated = createTestEntity(ID, UPDATED_NAME);
    mEntity2 = createTestEntity(ID_2, NAME_2);
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void canRetrieveById() {
    // Act
    mDao.insertDynamicLayer(mEntity);

    // Assert
    assertEqualToStrings(mEntity, mDao.getDynamicLayerById(ID));
  }

  @Test
  public void getAllDynamicLayersRetrievesAllLayers() {
    // Arrange
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntity2);

    // Act
    Object[] actuals = mDao.getAllDynamicLayers().toArray();

    // Assert
    DynamicLayerEntity[] expecteds = { mEntity, mEntity2 };
    assertEqualToStrings(expecteds, actuals);
  }

  @Test
  public void onConflictReplace() {
    // Act
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntityUpdated);

    // Assert
    assertEqualToStrings(mEntityUpdated, mDao.getDynamicLayerById(ID));
  }

  @Test
  public void latestDynamicLayerObservableIsUpdated() {
    // Act
    mDao.insertDynamicLayer(mEntity);
    TestSubscriber<String> testSubscriber =
        mDao.getLatestDynamicLayer().map(DynamicLayerEntity::toString).test();
    mDao.insertDynamicLayer(mEntity2);
    mDao.insertDynamicLayer(mEntity);

    // Assert
    testSubscriber.assertValues(mEntity.toString(), mEntity2.toString(), mEntity.toString());
  }
  */
}
