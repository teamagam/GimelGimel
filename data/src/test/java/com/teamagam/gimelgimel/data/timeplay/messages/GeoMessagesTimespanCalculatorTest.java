package com.teamagam.gimelgimel.data.timeplay.messages;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
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
public class GeoMessagesTimespanCalculatorTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private GeoMessagesTimespanCalculator mCalculator;
  private MessagesDao mMessagesDao;

  private void addNonGeoMessage(int date) {
    ChatMessageEntity entity = new ChatMessageEntity();
    entity.creationDate = new Date(date);
    mMessagesDao.insertMessage(entity);
  }

  private void fillGeoMessages(long... timestamps) {
    int i = 0;
    for (long timestamp : timestamps) {
      insertGeoMessage(Utils.createGeoChatMessageEntity("id" + (i++), timestamp));
    }
  }

  private void insertGeoMessage(ChatMessageEntity geoChatMessageEntity) {
    mMessagesDao.insertMessage(geoChatMessageEntity);
  }

  @Before
  public void setUp() throws Exception {
    mMessagesDao = mDbRule.getDb().messageDao();
    mCalculator = new GeoMessagesTimespanCalculator(mMessagesDao);
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