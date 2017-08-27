package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.teamagam.gimelgimel.data.common.DbTestUtils.getDB;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SearchMessagesDaoTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private MessagesDao mMessagesDao;
  private SearchMessagesDao mSearchMessagesDao;

  @Before
  public void setup() {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = getDB(context);
    mMessagesDao = mDb.messageDao();
    mSearchMessagesDao = mDb.searchMessagesDao();
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void TextMessageSearchTest() {
    //Arrange
    ChatMessageEntity chatMessage = mock(ChatMessageEntity.class);
    chatMessage.text = "static and benel are the best!";

    ChatMessageEntity chatMessage2 = mock(ChatMessageEntity.class);
    chatMessage2.text = "new york is very cool place to live in.";

    ChatMessageEntity chatMessage3 = mock(ChatMessageEntity.class);
    chatMessage3.text = "I don't like new york";
    mMessagesDao.insertMessage(chatMessage);
    mMessagesDao.insertMessage(chatMessage2);
    mMessagesDao.insertMessage(chatMessage3);

    String searchText = makeStringReadyForSearch("new york");

    //Act
    List<ChatMessageEntity> results = mSearchMessagesDao.getMessagesMatchingSearch(searchText);

    //Assert
    assertTrue(doesCollectionHasAllItems(results, chatMessage2, chatMessage3));
  }

  private String makeStringReadyForSearch(String text) {
    text = "%" + text + "%";
    return text;
  }

  private boolean doesCollectionHasAllItems(List<ChatMessageEntity> results,
      ChatMessageEntity... chatMessageEntities) {
    List<String> textMessagesResults = getTextMessagesList(results);
    for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
      if (!textMessagesResults.contains(chatMessageEntity.text)) {
        return false;
      }
    }
    return true;
  }

  private List<String> getTextMessagesList(List<ChatMessageEntity> results) {
    List<String> textMessagesResults = new ArrayList<>();
    for (ChatMessageEntity chatMessageEntity : results) {
      textMessagesResults.add(chatMessageEntity.text);
    }
    return textMessagesResults;
  }
}