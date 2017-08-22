package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.ChatMessageFeaturesToEntityFeatures;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.FeatureToEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessageFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesTextSearcher;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesTextSearcherImpl;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.teamagam.gimelgimel.data.common.DbTestUtils.getDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SearchMessagesDaoTest extends BaseTest {

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
    mMessagesTextSearcher = getMessagesTextSearcher();
    mMessagesEntityMapper = getMessagesEntityMapper();
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void geoMessageSearchTest() {
    //Arrange
    ChatMessageEntity chatMessage = mock(ChatMessageEntity.class);
    chatMessage.text = "static and benel are the best!";
    chatMessage.geoFeatureEntity = new GeoFeatureEntity();

    ChatMessageEntity chatMessage2 = mock(ChatMessageEntity.class);
    chatMessage2.text = "new york is very cool place to live in.";

    ChatMessageEntity chatMessage3 = mock(ChatMessageEntity.class);
    chatMessage3.text = "I don't like it.";
    chatMessage3.geoFeatureEntity = new GeoFeatureEntity();
    chatMessage3.geoFeatureEntity.id = "new york";

    //Act
    mMessagesDao.insertMessage(chatMessage);
    mMessagesDao.insertMessage(chatMessage2);
    mMessagesDao.insertMessage(chatMessage3);
    List<ChatMessage> results = mMessagesTextSearcher.searchMessagesByText("new york");

    //Assert
    assertThat(results, hasSize(2));
  }

  @Test
  public void geoMessageSearchHarderTest() {
    //Arrange
    ChatMessageEntity chatMessage = mock(ChatMessageEntity.class);
    chatMessage.text = "text: static and benel are the best!";
    chatMessage.geoFeatureEntity = new GeoFeatureEntity();

    ChatMessageEntity chatMessage2 = mock(ChatMessageEntity.class);
    chatMessage2.text = "new york is very cool place to live in.";

    ChatMessageEntity chatMessage3 = mock(ChatMessageEntity.class);
    chatMessage3.text = "I don't like it.";
    chatMessage3.geoFeatureEntity = new GeoFeatureEntity();
    chatMessage3.geoFeatureEntity.id = "new york";

    //Act
    mMessagesDao.insertMessage(chatMessage);
    mMessagesDao.insertMessage(chatMessage2);
    mMessagesDao.insertMessage(chatMessage3);
    List<ChatMessage> results = mMessagesTextSearcher.searchMessagesByText("new york");

    //Assert
    assertThat(results, hasSize(2));
  }

  private MessagesTextSearcher getMessagesTextSearcher() {
    GeometryDataMapper geometryDataMapper = new GeometryDataMapper();
    GeoEntityDataMapper geoEntityDataMapper = new GeoEntityDataMapper(geometryDataMapper);
    SymbolToStyleMapper symbolToStyleMapper = new SymbolToStyleMapper();
    GeoFeatureEntityMapper geoFeatureEntityMapper =
        new GeoFeatureEntityMapper(geoEntityDataMapper, geometryDataMapper, symbolToStyleMapper);
    MessageFeatureEntityMapper messageEntityMapper =
        new MessageFeatureEntityMapper(geoFeatureEntityMapper);
    FeatureToEntityMapper featureToEntityMapper = new FeatureToEntityMapper(geoFeatureEntityMapper);
    ChatMessageFeaturesToEntityFeatures chatMessageFeaturesToEntityFeatures =
        new ChatMessageFeaturesToEntityFeatures(featureToEntityMapper);
    return new MessagesTextSearcherImpl(mDb,
        new MessagesEntityMapper(messageEntityMapper, chatMessageFeaturesToEntityFeatures));
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