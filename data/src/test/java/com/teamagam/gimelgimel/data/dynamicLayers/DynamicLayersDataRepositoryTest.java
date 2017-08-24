package com.teamagam.gimelgimel.data.dynamicLayers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.common.DbTestUtils;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
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

import static com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils.assertEqualToStrings;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayersDataRepositoryTest extends BaseTest {

  public static final String ID_1 = "id_1";
  public static final String ID_2 = "id_2";
  public static final String NAME_1 = "name_1";
  public static final String NAME_2 = "name_2";

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
    mDb = DbTestUtils.getDB(context);
    mDao = mDb.dynamicLayerDao();
    mMapper = DynamicLayersTestUtils.createDynamicLayersEntityMapper();

    mRepo = new DynamicLayersDataRepository(mDao, mMapper);
    mLayer1 = new DynamicLayer(ID_1, NAME_1, 0, Collections.EMPTY_LIST);
    mLayer2 = new DynamicLayer(ID_2, NAME_2, 0, Collections.EMPTY_LIST);
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void emptyRepoEmitsNothingAndNotComplete() {
    // Assert
    mRepo.getObservable().test().assertNoErrors().assertNoValues().assertNotComplete();
  }

  @Test
  public void canRetrieveLayers() {
    // Act
    mRepo.put(mLayer1);
    mRepo.put(mLayer2);

    // Assert
    assertEqualToStrings(mLayer1, mRepo.getById(ID_1));
    assertEqualToStrings(mLayer2, mRepo.getById(ID_2));
  }

  @Test(expected = RuntimeException.class)
  public void nonExistingLayerThrowsException() {
    // Act
    mRepo.getById(ID_1);
  }

  @Test
  public void repoContainsOnlyPutLayer() {
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
    TestObserver<String> testObserver = mRepo.getObservable().map(DynamicLayer::toString).test();

    // Act
    mRepo.put(mLayer2);

    // Assert
    testObserver.assertValues(mLayer1.toString(), mLayer2.toString());
  }

  @Test
  public void persistenceBetweenInstances() {
    // Arrange
    mRepo.put(mLayer1);

    // Act
    DynamicLayersDataRepository newRepo = new DynamicLayersDataRepository(mDao, mMapper);

    // Assert
    assertTrue(newRepo.contains(ID_1));
  }

  @Test
  public void emitsUpdates() throws Exception {
    //Arrange
    mRepo.put(mLayer1);
    TestObserver<String> testObserver = mRepo.getObservable().map(DynamicLayer::toString).test();

    //Act
    DynamicLayer updatedLayer = new DynamicLayer(ID_1, "New Name", 0, Collections.EMPTY_LIST);
    mRepo.put(updatedLayer);

    //Assert
    testObserver.assertValues(mLayer1.toString(), updatedLayer.toString());
  }
}
