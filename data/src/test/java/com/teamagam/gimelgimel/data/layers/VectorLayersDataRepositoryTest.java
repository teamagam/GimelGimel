package com.teamagam.gimelgimel.data.layers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.VectorLayersEntityMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class VectorLayersDataRepositoryTest {

  // ****** NO FULL COVERAGE TEST SUITE ******

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private VectorLayersDataRepository mRepo;

  @Before
  public void setUp() throws Exception {
    mRepo = new VectorLayersDataRepository(mDbRule.getDb().vectorLayerDao(),
        mock(VectorLayersEntityMapper.class));
  }

  @Test
  public void emptyRepoEmitsNothingAndNotComplete() {
    mRepo.getVectorLayersObservable().test().assertNoErrors().assertNoValues().assertNotComplete();
  }
}
