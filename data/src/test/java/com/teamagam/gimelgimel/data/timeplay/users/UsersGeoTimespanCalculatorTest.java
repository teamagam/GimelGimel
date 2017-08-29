package com.teamagam.gimelgimel.data.timeplay.users;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.timeplay.Utils;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UsersGeoTimespanCalculatorTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private UsersGeoTimespanCalculator mCalculator;
  private UserLocationDao mUserLocationDao;

  private void fillUserLocations(long... timestamps) {
    int i = 0;
    for (long timestamp : timestamps) {
      insertUserLocation(Utils.createUserLocationEntity("id" + (i++), timestamp));
    }
  }

  private void insertUserLocation(UserLocationEntity userLocationEntity) {
    mUserLocationDao.insertUserLocation(userLocationEntity);
  }

  @Before
  public void setUp() throws Exception {
    mUserLocationDao = mDbRule.getDb().userLocationDao();
    mCalculator = new UsersGeoTimespanCalculator(mUserLocationDao);
  }

  @Test
  public void onEmptyDb_returnZeroDateMinimum() throws Exception {
    assertThat(mCalculator.getMinimumGeoItemDate(), is(new Date(0)));
  }

  @Test
  public void onEmptyDb_returnZeroDateMaximum() throws Exception {
    assertThat(mCalculator.getMaximumGeoItemDate(), is(new Date(0)));
  }

  @Test
  public void getMaximum() throws Exception {
    //Arrange
    fillUserLocations(0, 100, 400);

    //Act
    Date maximumGeoItemDate = mCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate.getTime(), is(400L));
  }

  @Test
  public void getMinimum() throws Exception {
    //Arrange
    fillUserLocations(5, 20, 50, 100);

    //Act
    Date minimumGeoItemDate = mCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(minimumGeoItemDate.getTime(), is(5L));
  }
}