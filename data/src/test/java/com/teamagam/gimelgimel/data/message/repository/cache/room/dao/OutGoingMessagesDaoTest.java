package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class OutGoingMessagesDaoTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private OutGoingMessagesDao mDao;

  @Before
  public void setup() {
    mDao = mDbRule.getDb().outgoingMessageDao();
  }

  @Test
  public void insertMessageTest() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    int insertAmount = 3;

    //Act
    for (int i = 1; i <= insertAmount; i++) {
      mDao.insertMessage(outGoingChatMessage);
    }

    //Assert
    assertThat(mDao.getMessagesByOutGoingId(), hasSize(insertAmount));
  }

  @Test
  public void deleteTopMessageTest() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    int insertAmount = 3;
    int deleteAmount = 2;

    //Act
    for (int i = 1; i <= insertAmount; i++) {
      mDao.insertMessage(outGoingChatMessage);
    }

    for (int i = 1; i <= deleteAmount; i++) {
      mDao.deleteTopMessage();
    }

    //Assert
    int messageLeftInQueueAmount = insertAmount - deleteAmount;
    assertThat(mDao.getMessagesByOutGoingId(), hasSize(messageLeftInQueueAmount));
  }

  @Test
  public void insertAndDeleteAllQueryTest() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    int insertAmount = 3;
    int deleteAmount = 2;

    //Act
    for (int i = 1; i <= insertAmount; i++) {
      mDao.insertMessage(outGoingChatMessage);
    }

    for (int i = 1; i <= deleteAmount; i++) {
      mDao.deleteTopMessage();
    }

    //Assert
    int messageLeftInQueueAmount = insertAmount - deleteAmount;
    assertThat(mDao.getMessagesByOutGoingId(), hasSize(messageLeftInQueueAmount));
  }

  @Test
  public void getLastMessageAfterInsertAndDeleteTest() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    int insertAmount = 3;
    int deleteAmount = 2;
    int reInsertAmount = 1;
    int lastMessageId = 1;

    //Act
    for (int i = 1; i <= insertAmount; i++) {
      mDao.insertMessage(outGoingChatMessage);
    }

    for (int i = 1; i <= deleteAmount; i++) {
      mDao.deleteTopMessage();
      lastMessageId++;
    }

    for (int i = 1; i <= reInsertAmount; i++) {
      mDao.insertMessage(outGoingChatMessage);
    }

    //Assert
    assertThat(mDao.getTopMessage().outGoingMessageId, is(lastMessageId));
  }

  @Test
  public void getLastMessageTest() {
    //Arrange
    OutGoingChatMessageEntity outGoingChatMessage = mock(OutGoingChatMessageEntity.class);
    int insertAmount = 2;
    int lastMessageId = 1;

    //Act
    for (int i = 1; i <= insertAmount; i++) {
      mDao.insertMessage(outGoingChatMessage);
    }

    //Assert
    assertThat(mDao.getTopMessage().outGoingMessageId, is(lastMessageId));
  }
}