package com.teamagam.gimelgimel.data.message.repository.search;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.SearchMessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.search.MessagesTextSearcher;
import java.util.List;
import javax.inject.Inject;

public class MessagesTextSearcherData implements MessagesTextSearcher {

  private SearchMessagesDao mSearchMessagesDao;
  private MessagesEntityMapper mMessagesEntityMapper;

  @Inject
  public MessagesTextSearcherData(SearchMessagesDao searchMessagesDao,
      MessagesEntityMapper messagesEntityMapper) {
    mSearchMessagesDao = searchMessagesDao;
    mMessagesEntityMapper = messagesEntityMapper;
  }

  @Override
  public List<ChatMessage> searchMessagesByText(String text) {
    String textForSearch = createContainsTextQuery(text);
    List<ChatMessageEntity> chatMessageEntitiesList =
        mSearchMessagesDao.getMessagesMatchingSearch(textForSearch);
    return Lists.transform(chatMessageEntitiesList,
        entity -> mMessagesEntityMapper.mapToDomain(entity));
  }

  private String createContainsTextQuery(String text) {
    text = "%" + text + "%";
    return text;
  }
}
