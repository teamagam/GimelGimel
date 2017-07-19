package com.teamagam.gimelgimel.data.dynamicLayers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import io.reactivex.observers.TestObserver;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayersDataRepositoryTest {

  public static final String ID_1 = "id";
  public static final String ID_2 = "id2";

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private AppDatabase mDb;
  private DynamicLayerDao mDao;
  private DynamicLayersEntityMapper mMapper;
  private DynamicLayersDataRepository mRepo;
  private DynamicLayer mLayer1;
  private DynamicLayer mLayer2;

  @Before
  public void setUp() {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
    mDao = mDb.dynamicLayerDao();
    mMapper = new DynamicLayersEntityMapper();

    mRepo = new DynamicLayersDataRepository(mDao, mMapper);
    mLayer1 = new DynamicLayer(ID_1, "name", Collections.EMPTY_LIST);
    mLayer2 = new DynamicLayer(ID_2, "name2", Collections.EMPTY_LIST);
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void canRetrieveLayers() {
    // Arrange

    // Act
    mRepo.put(mLayer1);
    mRepo.put(mLayer2);

    // Assert
    assertEquals(mLayer1, mRepo.getById(ID_1));
    assertEquals(mLayer2, mRepo.getById(ID_2));
  }

  @Test(expected = RuntimeException.class)
  public void nonExistingLayerThrowsException() {
    // Arrange

    // Act
    mRepo.getById(ID_1);
  }

  @Test
  public void repoContainsOnlyPutLayer() {
    // Arrange

    // Act
    mRepo.put(mLayer1);

    // Assert
    assertTrue(mRepo.contains(ID_1));
    assertFalse(mRepo.contains(ID_2));
  }

  @Test
  public void observableEmitsOldAndNewLayers() {
    // Arrange
    mRepo.put(mLayer1);
    TestObserver<DynamicLayer> testObserver = mRepo.getObservable().test();

    // Act
    mRepo.put(mLayer2);

    // Assert
    testObserver.assertValues(mLayer1, mLayer2);
  }

  @Test
  public void persistenceBetweenInstances() {
    // Arrange

    // Act
    mRepo.put(mLayer1);

    // Assert
    assertTrue(new DynamicLayersDataRepository(mDao, mMapper).contains(ID_1));
  }
}