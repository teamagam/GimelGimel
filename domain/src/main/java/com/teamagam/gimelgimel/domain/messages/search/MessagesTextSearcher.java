package com.teamagam.gimelgimel.domain.messages.search;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import java.util.List;

public interface MessagesTextSearcher {
  List<ChatMessage> searchMessagesByText(String text);
}
