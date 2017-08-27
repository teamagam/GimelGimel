package com.teamagam.gimelgimel.data.timeplay;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.common.DbTestUtils;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.UserLocationsEntityMapper;
import com.teamagam.gimelgimel.data.timeplay.users.UserGeoSnapshoter;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UserGeoSnapshoterTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private AppDatabase mDb;
  private UserGeoSnapshoter mSnapshoter;
  private UserLocationDao mUserLocationDao;

  private UserLocationsEntityMapper getUserLocationsEntityMapper() {
    return new UserLocationsEntityMapper(new GeometryDataMapper());
  }

  private void insertUserLocation(String userId, long timestamp) {
    mUserLocationDao.insertUserLocation(Utils.createUserLocationEntity(userId, timestamp));
  }

  private String getUserEntityId(String userId, long timestamp) {
    return String.format("%s_%s", userId, timestamp);
  }

  @Before
  public void setUp() throws Exception {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = DbTestUtils.getDB(context);

    mUserLocationDao = mDb.userLocationDao();
    mSnapshoter = new UserGeoSnapshoter(mUserLocationDao, getUserLocationsEntityMapper());
  }

  @After
  public void tearDown() throws Exception {
    mDb.close();
  }

  @Test
  public void onEmptyDb_expectEmptyResult() throws Exception {
    //Act
    Collection<GeoEntity> res = mSnapshoter.snapshot(0);

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void retrievesUserLocation() throws Exception {
    //Arrange
    insertUserLocation("id1", 0);

    //Act
    List<GeoEntity> snapshot = mSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is(getUserEntityId("id1", 0)));
  }

  @Test
  public void filterNewerUserLocations() throws Exception {
    //Arrange
    insertUserLocation("id1", 0);
    insertUserLocation("id2", 1);

    //Act
    List<GeoEntity> snapshot = mSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is(getUserEntityId("id1", 0)));
  }
}