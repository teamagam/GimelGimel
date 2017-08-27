package com.teamagam.gimelgimel.data.timeplay;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.common.DbTestUtils;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.timeplay.messages.GeoMessagesTimespanCalculator;
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
public class GeoMessagesTimespanCalculatorTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private GeoMessagesTimespanCalculator mCalculator;

  private void addNonGeoMessage(int date) {
    ChatMessageEntity entity = new ChatMessageEntity();
    entity.creationDate = new Date(date);
    mDb.messageDao().insertMessage(entity);
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

  @Before
  public void setUp() throws Exception {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = DbTestUtils.getDB(context);
    mCalculator = new GeoMessagesTimespanCalculator(mDb.messageDao());
  }

  @After
  public void tearDown() throws Exception {
    mDb.close();
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
  public void nonGeoMessageIgnoredOnMinimum() throws Exception {
    //Arrange
    addNonGeoMessage(5);
    fillGeoMessages(10, 20, 30);

    //Act
    Date minimumGeoItemDate = mCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(minimumGeoItemDate.getTime(), is(10L));
  }

  @Test
  public void nonGeoMessageIgnoredOnMaximum() throws Exception {
    //Arrange
    addNonGeoMessage(50);
    fillGeoMessages(10, 20, 30);

    //Act
    Date maximumGeoItemDate = mCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate.getTime(), is(30L));
  }

  @Test
  public void getMinimum() throws Exception {
    //Arrange
    fillGeoMessages(5, 10, 20, 30);

    //Act
    Date minimumGeoItemDate = mCalculator.getMinimumGeoItemDate();

    //Assert
    assertThat(minimumGeoItemDate.getTime(), is(5L));
  }

  @Test
  public void getMaximum() throws Exception {
    //Arrange
    fillGeoMessages(0, 10, 20, 40);

    //Act
    Date maximumGeoItemDate = mCalculator.getMaximumGeoItemDate();

    //Assert
    assertThat(maximumGeoItemDate.getTime(), is(40L));
  }
}