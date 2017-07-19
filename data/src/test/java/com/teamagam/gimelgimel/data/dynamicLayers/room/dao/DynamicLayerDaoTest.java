package com.teamagam.gimelgimel.data.dynamicLayers.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayerDaoTest {

  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String UPDATED_NAME = "updatedName";
  public static final String ID_2 = "id2";
  public static final String NAME_2 = "name2";
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
    mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
    mDao = mDb.dynamicLayerDao();

    mEntity = new DynamicLayerEntity();
    mEntity.id = ID;
    mEntity.name = NAME;
    //entity.entities = "???";

    mEntityUpdated = new DynamicLayerEntity();
    mEntityUpdated.id = ID;
    mEntityUpdated.name = UPDATED_NAME;
    //entity.entities = "???";

    mEntity2 = new DynamicLayerEntity();
    mEntity2.id = ID_2;
    mEntity2.name = NAME_2;
    //entity.entities = "???";
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void canRetrieveAfterInsert() {
    // Arrange

    // Act
    mDao.insertDynamicLayer(mEntity);

    // Assert
    assertEquals(mEntity, mDao.getDynamicLayerById(ID));
  }

  @Test
  public void getAllDynamicLayersRetrievesAllLayers() {
    // Arrange

    // Act
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntity2);

    // Assert
    DynamicLayerEntity[] expecteds = { mEntity, mEntity2 };
    Object[] actuals = mDao.getAllDynamicLayers().toArray();

    assertArrayEquals(expecteds, actuals);
  }

  @Test
  public void onConflictReplace() {
    // Arrange

    // Act
    mDao.insertDynamicLayer(mEntity);
    mDao.insertDynamicLayer(mEntityUpdated);

    // Assert
    assertEquals(mEntityUpdated, mDao.getDynamicLayerById(ID));
  }

  @Test
  public void latestDynamicLayerObservableIsUpdated() {
    // Arrange

    // Act
    mDao.insertDynamicLayer(mEntity);
    TestSubscriber<DynamicLayerEntity> testSubscriber = mDao.getLatestDynamicLayer().test();
    mDao.insertDynamicLayer(mEntity2);
    mDao.insertDynamicLayer(mEntity);

    // Assert
    testSubscriber.assertValues(mEntity, mEntity2, mEntity);
  }
}