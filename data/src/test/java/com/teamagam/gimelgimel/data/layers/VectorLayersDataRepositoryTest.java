package com.teamagam.gimelgimel.data.layers;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class VectorLayersDataRepositoryTest {
  /*

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
  */
}
