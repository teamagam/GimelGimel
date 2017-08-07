package com.teamagam.gimelgimel.data.layers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.VectorLayersEntityMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class VectorLayersDataRepositoryTest {

  // ****** NO FULL COVERAGE TEST SUITE ******

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private VectorLayersDataRepository mRepo;

  @Before
  public void setUp() throws Exception {
    mDb = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application.getApplicationContext(),
        AppDatabase.class).allowMainThreadQueries().build();
    mRepo =
        new VectorLayersDataRepository(mDb.vectorLayerDao(), mock(VectorLayersEntityMapper.class));
  }

  @After
  public void tearDown() throws Exception {
    mDb.close();
  }

  @Test
  public void emptyRepoEmitsNothingAndNotComplete() {
    mRepo.getVectorLayersObservable().test().assertNoErrors().assertNoValues().assertNotComplete();
  }
}
