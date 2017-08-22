package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.SearchMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MessagesTextSearcherImpl implements MessagesTextSearcher {

  private SearchMessagesDao mSearchMessagesDao;
  private MessagesEntityMapper mMessagesEntityMapper;

  @Inject
  public MessagesTextSearcherImpl(AppDatabase appDatabase,
      MessagesEntityMapper messagesEntityMapper) {
    mSearchMessagesDao = appDatabase.searchMessagesDao();
    mMessagesEntityMapper = messagesEntityMapper;
  }

  @Override
  public List<ChatMessage> searchMessagesByText(String text) {
    String textForSearch = makeStringReadyForSearch(text);
    List<ChatMessageEntity> chatMessageEntitiesList =
        mSearchMessagesDao.getMessagesMatchingSearch(textForSearch);
    return mapIterableToDomain(chatMessageEntitiesList);
  }

  private String makeStringReadyForSearch(String text) {
    text = "%" + text + "%";
    return text;
  }

  private List<ChatMessage> mapIterableToDomain(List<ChatMessageEntity> chatMessageEntities) {
    List<ChatMessage> chatMessagesList = new ArrayList<>();
    for (ChatMessageEntity messageEntitie : chatMessageEntities) {
      chatMessagesList.add(mMessagesEntityMapper.mapToDomain(messageEntitie));
    }
    return chatMessagesList;
  }
}
