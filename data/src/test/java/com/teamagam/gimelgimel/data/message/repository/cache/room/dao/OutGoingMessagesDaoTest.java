package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils.getDB;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class OutGoingMessagesDaoTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private OutGoingMessagesDao mDao;
  private MessagesEntityMapper mMapper;

  @Before
  public void setup() {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = getDB(context);
    mDao = mDb.outgoingMessageDao();
  }

  @Test
  public void insertMessage() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);

    //Act
    mDao.insertMessage(outGoingChatMessage);

    //Assert
    assertTrue("tudu bom", mDao.getMessagesByOutGoingId().size() == 1);
  }

  @Test
  public void deleteTopMessage() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    OutGoingChatMessageEntity outGoingChatMessage2 = mock(OutGoingChatMessageEntity.class);
    OutGoingChatMessageEntity outGoingChatMessage3 = mock(OutGoingChatMessageEntity.class);

    //Act
    mDao.insertMessage(outGoingChatMessage);
    mDao.insertMessage(outGoingChatMessage2);
    mDao.insertMessage(outGoingChatMessage3);
    mDao.deleteTopMessage();
    mDao.deleteTopMessage();

    //Assert
    assertTrue("tudu bom", mDao.getMessagesByOutGoingId().get(0).outGoingMessageId == 3);
  }


  @Test
  public void deleteAllMessages() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    OutGoingChatMessageEntity outGoingChatMessage2 = mock(OutGoingChatMessageEntity.class);
    OutGoingChatMessageEntity outGoingChatMessage3 = mock(OutGoingChatMessageEntity.class);

    //Act
    mDao.insertMessage(outGoingChatMessage);
    mDao.insertMessage(outGoingChatMessage2);
    mDao.insertMessage(outGoingChatMessage3);
    mDao.deleteTopMessage();
    mDao.deleteTopMessage();
    mDao.deleteTopMessage();

    //Assert
    assertTrue("tudu bom", mDao.getMessagesByOutGoingId().size() == 0);
  }
}