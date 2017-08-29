package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.ChatMessageFeaturesToEntityFeatures;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.FeatureToEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessageFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.data.message.repository.search.MessagesTextSearcherData;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.search.MessagesTextSearcher;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.teamagam.gimelgimel.data.common.DbTestUtils.getDB;
import static org.assertj.core.api.Assertions.assertThat;

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
    mMessagesEntityMapper = getMessagesEntityMapper();
    mMessagesTextSearcher =
        new MessagesTextSearcherData(mDb.searchMessagesDao(), mMessagesEntityMapper);
  }

  @After
  public void tearDown() {
    mDb.close();
  }

  @Test
  public void TextMessageSearchTest() {
    //Arrange
    ChatMessage chatMessage =
        new ChatMessage("1", "Dany", new Date(), new TextFeature("No on has ever call me danny"));
    ChatMessage chatMessage2 =
        new ChatMessage("2", "John", new Date(), new TextFeature("no Danny"));
    ChatMessage chatMessage3 =
        new ChatMessage("3", "John", new Date(), new TextFeature("How about my queen?"));

    mMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(chatMessage));
    mMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(chatMessage2));
    mMessagesDao.insertMessage(mMessagesEntityMapper.mapToEntity(chatMessage3));

    String searchText = "new york";

    //Act
    List<ChatMessage> results = mMessagesTextSearcher.searchMessagesByText(searchText);

    //Assert
    assertThat(containsItems(results, chatMessage2, chatMessage3));
  }

  private boolean containsItems(List<ChatMessage> results, ChatMessage... chatMessages) {
    List<String> textMessagesResults =
        Lists.transform(results, input -> input.getFeatureByType(TextFeature.class).getText());
    for (ChatMessage chatMessage : chatMessages) {
      if (!textMessagesResults.contains(
          chatMessage.getFeatureByType(TextFeature.class).getText())) {
        return false;
      }
    }
    return true;
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