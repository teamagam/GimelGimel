package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.ChatMessageFeaturesToEntityFeatures;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.FeatureToEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessageFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.data.message.repository.search.DataMessagesDaoSearcher;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.search.MessagesTextSearcher;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.teamagam.gimelgimel.data.common.DbTestUtils.getDB;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SearchMessagesDaoTest extends BaseTest {

  private static final String SENDER_ID = "const";

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private MessagesDao mMessagesDao;
  private MessagesTextSearcher mMessagesTextSearcher;
  private MessagesEntityMapper mMessagesEntityMapper;

  @Before
  public void setup() {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = getDB(context);
    mMessagesDao = mDb.messageDao();
    mMessagesEntityMapper = getMessagesEntityMapper();
    mMessagesTextSearcher =
        new DataMessagesDaoSearcher(mDb.searchMessagesDao(), mMessagesEntityMapper);
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void queryChatMessages() {
    //Arrange
    ChatMessage expected1 = createAndInsertMessage("No one has ever call me Danny");
    ChatMessage expected2 = createAndInsertMessage("OK, no Danny");
    createAndInsertMessage("How about my queen");

    //Act
    List<ChatMessage> results = mMessagesTextSearcher.searchMessagesByText("Danny");

    //Assert
    assertThat(results, contains(expected1, expected2));
  }

  @Test
  public void onEmptyQuery_returnEmptyList() throws Exception {
    //Arrange
    createAndInsertMessage("some text");
    createAndInsertMessage("");
    createAndInsertMessage(" ");

    //Act
    List<ChatMessage> res = mMessagesTextSearcher.searchMessagesByText("");

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void onNullQuery_returnEmptyList() throws Exception {
    //Arrange
    createAndInsertMessage("");
    createAndInsertMessage(" ");
    createAndInsertMessage("Text");

    //Act
    List<ChatMessage> res = mMessagesTextSearcher.searchMessagesByText(null);

    //Assert
    assertThat(res, empty());
  }

  private ChatMessage createAndInsertMessage(String text) {
    ChatMessage chatMessage =
        new ChatMessage((generateId()), SENDER_ID, new Date(), new TextFeature(text));
    mMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(chatMessage));
    return chatMessage;
  }

  private String generateId() {
    return UUID.randomUUID().toString();
  }

  private MessagesEntityMapper getMessagesEntityMapper() {
    GeometryDataMapper geometryDataMapper = new GeometryDataMapper();
    GeoEntityDataMapper geoEntityDataMapper = new GeoEntityDataMapper(geometryDataMapper);
    SymbolToStyleMapper symbolToStyleMapper = new SymbolToStyleMapper();
    GeoFeatureEntityMapper geoFeatureEntityMapper =
        new GeoFeatureEntityMapper(geoEntityDataMapper, geometryDataMapper, symbolToStyleMapper);
    MessageFeatureEntityMapper messageFeatureEntityMapper =
        new MessageFeatureEntityMapper(geoFeatureEntityMapper);
    FeatureToEntityMapper featureToEntityMapper = new FeatureToEntityMapper(geoFeatureEntityMapper);
    ChatMessageFeaturesToEntityFeatures chatMessageFeaturesToEntityFeatures =
        new ChatMessageFeaturesToEntityFeatures(featureToEntityMapper);
    return new MessagesEntityMapper(messageFeatureEntityMapper,
        chatMessageFeaturesToEntityFeatures);
  }
}