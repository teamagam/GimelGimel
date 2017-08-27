package com.teamagam.gimelgimel.data.timeplay;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.common.DbTestUtils;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DataGeoTimespanCalculatorTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private DataGeoTimespanCalculator mDataGeoTimespanCalculator;

  @Before
  public void setUp() throws Exception {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = DbTestUtils.getDB(context);
    mDataGeoTimespanCalculator =
        new DataGeoTimespanCalculator(new GeoMessagesTimespanCalculator(mDb.messageDao()),
            new UsersGeoTimespanCalculator(mDb.userLocationDao()));
  }

  @After
  public void tearDown() throws Exception {
    mDb.close();
  }

  @Test
  public void onEmptyDb_returnMaxDateZero() throws Exception {
    //Act
    Date maximumGeoItemDate = mDataGeoTimespanCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate, is(new Date(0)));
  }

  @Test
  public void onEmptyDb_returnMinDateZero() throws Exception {
    //Act
    Date maximumGeoItemDate = mDataGeoTimespanCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate, is(new Date(0)));
  }

  @Test
  public void tieForMinimum() throws Exception {
    //Arrange
    fillGeoMessages(0, 100, 300, 400);
    fillUserLocations(0, 50, 66, 99);

    //Act
    Date minimumGeoItemDate = mDataGeoTimespanCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(minimumGeoItemDate.getTime(), is(0L));
  }

  @Test
  public void tieForMaximum() throws Exception {
    //Arrange
    fillGeoMessages(0, 100, 500);
    fillUserLocations(10, 150, 500);

    //Act
    Date maximumGeoItemDate = mDataGeoTimespanCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate.getTime(), is(500L));
  }

  private void fillGeoMessages(long... timestamps) {
    int i = 0;
    for (long timestamp : timestamps) {
      insertGeoMessage(Utils.createGeoChatMessageEntity("id" + (i++), timestamp));
    }
  }

  private void insertGeoMessage(ChatMessageEntity geoChatMessageEntity) {
    mDb.messageDao().insertMessage(geoChatMessageEntity);
  }

  private void fillUserLocations(long... timestamps) {
    int i = 0;
    for (long timestamp : timestamps) {
      insertUserLocation(Utils.createUserLocationEntity("id" + (i++), timestamp));
    }
  }

  private void insertUserLocation(UserLocationEntity userLocationEntity) {
    mDb.userLocationDao().insertUserLocation(userLocationEntity);
  }
}